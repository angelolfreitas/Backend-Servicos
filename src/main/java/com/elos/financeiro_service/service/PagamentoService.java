package com.elos.financeiro_service.service;

import com.elos.financeiro_service.dto.*;
import com.elos.financeiro_service.financeiro.ComissaoPagamento;
import com.elos.financeiro_service.financeiro.ContratacaoRef;
import com.elos.financeiro_service.financeiro.EtapaPagamento;
import com.elos.financeiro_service.financeiro.Pagamento;
import com.elos.financeiro_service.repository.ComissaoPagamentoRepository;
import com.elos.financeiro_service.repository.ContratacaoRefRepository;
import com.elos.financeiro_service.repository.EtapaPagamentoRepository;
import com.elos.financeiro_service.repository.PagamentoRepository;
import com.elos.financeiro_service.simulacao.GatewayPagamento;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

///Logica coracao do sistema
@Service
@RequiredArgsConstructor
public class PagamentoService {
    private static final BigDecimal CEM = BigDecimal.valueOf(100);

    private final PagamentoRepository pagamentos;
    private final EtapaPagamentoRepository etapas;
    private final ComissaoPagamentoRepository comissoes;
    private final ContratacaoRefRepository refs;
    private final GatewayPagamento gateway;
    private final ApplicationEventPublisher events;

    @Value("${app.comissao.percentual}")
    private BigDecimal percentualComissao;

    @Transactional
    public PagamentoResponse pagarEtapaInicial(Long idUsuario, Long idContratacao, CobrancaRequest req) {
        ContratacaoRef ref = refs.findById(idContratacao)
                .orElseThrow(() -> erro(HttpStatus.NOT_FOUND, "Contratação não encontrada"));
        if (!ref.getIdCliente().equals(idUsuario))
            throw erro(HttpStatus.FORBIDDEN, "Só o cliente da contratação paga");
        if (!"AGUARDANDO_PAGAMENTO".equals(ref.getStatus()))
            throw erro(HttpStatus.CONFLICT, "A contratação não está aguardando pagamento");
        if (pagamentos.findByIdContratacao(idContratacao).isPresent())
            throw erro(HttpStatus.CONFLICT, "Pagamento já registrado");
        if (ref.getValor() == null || ref.getValor().signum() <= 0)
            throw erro(HttpStatus.CONFLICT, "Contratação sem valor definido");

        BigDecimal total = ref.getValor();
        BigDecimal valor1 = total.divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
        BigDecimal valor2 = total.subtract(valor1);
        BigDecimal comissao = total.multiply(percentualComissao).divide(CEM, 2, RoundingMode.HALF_UP);

        gateway.cobrar(valor1, req.formaPagamento(), req.tokenPagamento());   // 402 se recusado

        Pagamento p = new Pagamento();
        p.setIdContratacao(idContratacao);
        p.setIdCliente(ref.getIdCliente());
        p.setIdPrestador(ref.getIdPrestador());
        p.setValorTotal(total);
        p.setValorLiquido(total.subtract(comissao));
        p.setFormaPagamento(req.formaPagamento());
        p.setStatus("ETAPA1_RETIDA");
        p.setData(LocalDateTime.now());
        pagamentos.save(p);

        etapas.save(etapa(p, 1, valor1, "RETIDA"));
        etapas.save(etapa(p, 2, valor2, "PENDENTE"));

        ComissaoPagamento c = new ComissaoPagamento();
        c.setIdPagamento(p.getIdPagamento());
        c.setPercentual(percentualComissao);
        c.setValor(comissao);
        comissoes.save(c);

        events.publishEvent(new PagamentoRetido(idContratacao));
        return resposta(p);
    }

    @Transactional
    public PagamentoResponse pagarEtapaFinal(Long idUsuario, Long idContratacao, CobrancaRequest req) {
        Pagamento p = pagamentos.findByIdContratacao(idContratacao)
                .orElseThrow(() -> erro(HttpStatus.NOT_FOUND, "Pagamento não encontrado"));
        if (!p.getIdCliente().equals(idUsuario))
            throw erro(HttpStatus.FORBIDDEN, "Só o cliente da contratação paga");
        if (!"AGUARDANDO_ETAPA_FINAL".equals(p.getStatus()))
            throw erro(HttpStatus.CONFLICT, "A etapa final só pode ser paga depois da conclusão do serviço");

        List<EtapaPagamento> lista = etapas.findByIdPagamentoOrderByNumero(p.getIdPagamento());
        gateway.cobrar(lista.get(1).getValor(), req.formaPagamento(), req.tokenPagamento());

        LocalDateTime agora = LocalDateTime.now();
        lista.forEach(e -> { e.setStatus("LIBERADA"); e.setDataLiberacao(agora); });
        p.setStatus("LIBERADO");
        gateway.repassar(p.getIdPrestador(), p.getValorLiquido());

        ComissaoPagamento c = comissoes.findByIdPagamento(p.getIdPagamento()).orElseThrow();
        events.publishEvent(new PagamentoLiberado(idContratacao, p.getIdPrestador(), p.getValorTotal(),
                c.getValor(), p.getValorLiquido(), agora.toLocalDate()));
        return resposta(p);
    }

    // chamados pelo listener de contratação (idempotentes: só agem no status esperado)
    @Transactional
    public void aoConcluir(Long idContratacao) {
        pagamentos.findByIdContratacao(idContratacao)
                .filter(p -> "ETAPA1_RETIDA".equals(p.getStatus()))
                .ifPresent(p -> p.setStatus("AGUARDANDO_ETAPA_FINAL"));
    }

    @Transactional
    public void aoCancelar(Long idContratacao) {
        pagamentos.findByIdContratacao(idContratacao)
                .filter(p -> "ETAPA1_RETIDA".equals(p.getStatus()))
                .ifPresent(p -> {
                    List<EtapaPagamento> lista = etapas.findByIdPagamentoOrderByNumero(p.getIdPagamento());
                    gateway.reembolsar(p.getIdCliente(), lista.get(0).getValor());
                    lista.get(0).setStatus("REEMBOLSADA");
                    lista.get(1).setStatus("CANCELADA");
                    p.setStatus("REEMBOLSADO");
                });
    }

    public PagamentoResponse consultar(Long idContratacao, Long idUsuario) {
        Pagamento p = pagamentos.findByIdContratacao(idContratacao)
                .orElseThrow(() -> erro(HttpStatus.NOT_FOUND, "Pagamento não encontrado"));
        if (!p.getIdCliente().equals(idUsuario) && !p.getIdPrestador().equals(idUsuario))
            throw erro(HttpStatus.FORBIDDEN, "Pagamento de outros usuários");
        return resposta(p);
    }

    // RF11: saldo líquido do prestador
    public SaldoResponse saldo(Long idUsuario) {
        return new SaldoResponse(
                pagamentos.somarLiquido(idUsuario, List.of("LIBERADO")),
                pagamentos.somarLiquido(idUsuario, List.of("ETAPA1_RETIDA", "AGUARDANDO_ETAPA_FINAL")));
    }

    // ---- auxiliares ----
    private EtapaPagamento etapa(Pagamento p, int numero, BigDecimal valor, String status) {
        EtapaPagamento e = new EtapaPagamento();
        e.setIdPagamento(p.getIdPagamento());
        e.setNumero(numero);
        e.setPercentual(BigDecimal.valueOf(50).setScale(2));
        e.setValor(valor);
        e.setStatus(status);
        return e;
    }

    private PagamentoResponse resposta(Pagamento p) {
        ComissaoPagamento c = comissoes.findByIdPagamento(p.getIdPagamento()).orElseThrow();
        List<EtapaResponse> lista = etapas.findByIdPagamentoOrderByNumero(p.getIdPagamento()).stream()
                .map(e -> new EtapaResponse(e.getNumero(), e.getPercentual(), e.getValor(),
                        e.getStatus(), e.getDataLiberacao()))
                .toList();
        return new PagamentoResponse(p.getIdContratacao(), p.getValorTotal(), p.getFormaPagamento(),
                p.getStatus(), c.getPercentual(), c.getValor(), p.getValorLiquido(), p.getData(), lista);
    }

    private ResponseStatusException erro(HttpStatus s, String msg) {
        return new ResponseStatusException(s, msg);
    }
}