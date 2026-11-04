package com.elos.negociacao_service;

import com.elos.negociacao_service.dto.AceiteRequest;
import com.elos.negociacao_service.dto.ContratacaoAtualizada;
import com.elos.negociacao_service.dto.ContratacaoResponse;
import com.elos.negociacao_service.dto.PropostaRequest;
import com.elos.negociacao_service.negociacao.Contratacao;
import com.elos.negociacao_service.negociacao.Proposta;
import com.elos.negociacao_service.negociacao.ServicoRef;
import com.elos.negociacao_service.negociacao.StatusContratacao;
import com.elos.negociacao_service.repository.ContratacaoRepository;
import com.elos.negociacao_service.repository.PropostaRepository;
import com.elos.negociacao_service.repository.ServicoRefRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NegociacaoService {
    private final PropostaRepository propostas;
    private final ContratacaoRepository contratacoes;
    private final ServicoRefRepository servicos;
    private final ApplicationEventPublisher events;

    @Transactional
    public ContratacaoResponse propor(Long idCliente, PropostaRequest req) {
        ServicoRef s = servicos.findById(req.idServico())
                .filter(x -> "ATIVO".equals(x.getStatus()))
                .orElseThrow(() -> erro(HttpStatus.NOT_FOUND, "Serviço indisponível"));
        if (s.getIdUsuarioPrestador().equals(idCliente))
            throw erro(HttpStatus.BAD_REQUEST, "Você não pode contratar o próprio serviço");
        if (req.validade() != null && req.validade().isBefore(LocalDate.now()))
            throw erro(HttpStatus.BAD_REQUEST, "Validade no passado");

        Proposta p = new Proposta();
        p.setIdCliente(idCliente);
        p.setIdPrestador(s.getIdUsuarioPrestador());
        p.setIdServico(s.getIdServico());
        p.setValor(req.valor() != null ? req.valor() : s.getValor());
        p.setDataInicio(req.dataInicio());
        p.setValidade(req.validade());
        p.setDescricao(req.descricao());
        p.setStatus("PENDENTE");
        propostas.save(p);

        Contratacao c = new Contratacao();
        c.setIdCliente(idCliente);
        c.setIdPrestador(p.getIdPrestador());
        c.setIdServico(p.getIdServico());
        c.setIdProposta(p.getIdProposta());
        c.setStatus(StatusContratacao.NEGOCIANDO.name());
        contratacoes.save(c);

        publicar(c);
        return resposta(c, p);
    }

    @Transactional
    public ContratacaoResponse aceitar(Long id, Long idUsuario, AceiteRequest req) {
        Contratacao c = buscar(id);
        exigirPrestador(c, idUsuario);
        exigirStatus(c, StatusContratacao.NEGOCIANDO);
        Proposta p = proposta(c);
        if (p.getValidade() != null && p.getValidade().isBefore(LocalDate.now()))
            throw erro(HttpStatus.CONFLICT, "Proposta expirada");
        LocalDate inicio = p.getDataInicio() != null ? p.getDataInicio() : LocalDate.now();
        if (req.dataFinalizacaoPrevista() == null || req.dataFinalizacaoPrevista().isBefore(inicio))
            throw erro(HttpStatus.BAD_REQUEST, "Prazo de entrega inválido");

        p.setStatus("ACEITA");
        c.setStatus(StatusContratacao.EM_EXECUCAO.name());
        c.setDataInicio(inicio);
        c.setDataFinalizacaoPrevista(req.dataFinalizacaoPrevista());
        propostas.save(p);
        contratacoes.save(c);
        publicar(c);
        return resposta(c, p);
    }

    @Transactional
    public ContratacaoResponse recusar(Long id, Long idUsuario) {
        Contratacao c = buscar(id);
        exigirPrestador(c, idUsuario);
        exigirStatus(c, StatusContratacao.NEGOCIANDO);
        Proposta p = proposta(c);
        p.setStatus("RECUSADA");
        c.setStatus(StatusContratacao.CANCELADA.name());
        propostas.save(p);
        contratacoes.save(c);
        publicar(c);
        return resposta(c, p);
    }

    @Transactional
    public ContratacaoResponse cancelar(Long id, Long idUsuario) {
        Contratacao c = buscar(id);
        exigirParticipante(c, idUsuario);
        if (!StatusContratacao.NEGOCIANDO.name().equals(c.getStatus())
                && !StatusContratacao.EM_EXECUCAO.name().equals(c.getStatus()))
            throw erro(HttpStatus.CONFLICT, "Contratação não pode mais ser cancelada");
        Proposta p = proposta(c);
        if ("PENDENTE".equals(p.getStatus())) {
            p.setStatus("CANCELADA");
            propostas.save(p);
        }
        c.setStatus(StatusContratacao.CANCELADA.name());
        contratacoes.save(c);
        publicar(c);
        return resposta(c, p);
    }

    @Transactional
    public ContratacaoResponse concluir(Long id, Long idUsuario) {
        Contratacao c = buscar(id);
        if (!c.getIdCliente().equals(idUsuario))
            throw erro(HttpStatus.FORBIDDEN, "Só o cliente confirma a conclusão");
        exigirStatus(c, StatusContratacao.EM_EXECUCAO);
        c.setStatus(StatusContratacao.CONCLUIDA.name());
        c.setDataFinalizacao(LocalDate.now());
        contratacoes.save(c);
        publicar(c);
        return resposta(c, proposta(c));
    }

    public List<ContratacaoResponse> minhas(Long idUsuario) {
        return contratacoes.findByIdClienteOrIdPrestadorOrderByIdContratacaoDesc(idUsuario, idUsuario)
                .stream().map(c -> resposta(c, proposta(c))).toList();
    }

    public ContratacaoResponse detalhar(Long id, Long idUsuario) {
        Contratacao c = buscar(id);
        exigirParticipante(c, idUsuario);
        return resposta(c, proposta(c));
    }

    // ---- auxiliares ----
    private Contratacao buscar(Long id) {
        return contratacoes.findById(id)
                .orElseThrow(() -> erro(HttpStatus.NOT_FOUND, "Contratação não encontrada"));
    }
    private Proposta proposta(Contratacao c) {
        return propostas.findById(c.getIdProposta())
                .orElseThrow(() -> erro(HttpStatus.NOT_FOUND, "Proposta não encontrada"));
    }
    private void exigirPrestador(Contratacao c, Long idUsuario) {
        if (!c.getIdPrestador().equals(idUsuario))
            throw erro(HttpStatus.FORBIDDEN, "Só o prestador pode fazer isso");
    }
    private void exigirParticipante(Contratacao c, Long idUsuario) {
        if (!c.getIdCliente().equals(idUsuario) && !c.getIdPrestador().equals(idUsuario))
            throw erro(HttpStatus.FORBIDDEN, "Contratação de outros usuários");
    }
    private void exigirStatus(Contratacao c, StatusContratacao esperado) {
        if (!esperado.name().equals(c.getStatus()))
            throw erro(HttpStatus.CONFLICT, "Status atual é " + c.getStatus());
    }
    private void publicar(Contratacao c) {
        events.publishEvent(new ContratacaoAtualizada(c.getIdContratacao(), c.getIdCliente(),
                c.getIdPrestador(), c.getIdServico(), c.getStatus()));
    }
    private RuntimeException erro(HttpStatus status, String msg) {
        return new ResponseStatusException(status, msg);
    }
    private ContratacaoResponse resposta(Contratacao c, Proposta p) {
        return new ContratacaoResponse(c.getIdContratacao(), c.getIdCliente(), c.getIdPrestador(),
                c.getIdServico(), c.getIdProposta(), p.getValor(), p.getDescricao(),
                c.getDataInicio(), c.getDataFinalizacaoPrevista(), c.getDataFinalizacao(), c.getStatus());
    }
}