package com.elos.reputacao_service.service;

import com.elos.reputacao_service.dto.*;
import com.elos.reputacao_service.repository.AvaliacaoRepository;
import com.elos.reputacao_service.repository.ContratacaoRefRepository;
import com.elos.reputacao_service.reputacao.Avaliacao;
import com.elos.reputacao_service.reputacao.ContratacaoRef;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AvaliacaoService {
    private final AvaliacaoRepository avaliacoes;
    private final ContratacaoRefRepository refs;
    private final ApplicationEventPublisher events;

    @Transactional
    public AvaliacaoResponse avaliar(Long idUsuario, AvaliacaoRequest req) {
        ContratacaoRef ref = ref(req.idContratacao());
        exigirParticipante(ref, idUsuario);
        if (!"CONCLUIDA".equals(ref.getStatus()))
            throw erro(HttpStatus.CONFLICT, "Só é possível avaliar contratações concluídas");
        if (avaliacoes.existsByIdContratacaoAndIdUsuarioAvaliador(req.idContratacao(), idUsuario))
            throw erro(HttpStatus.CONFLICT, "Você já avaliou esta contratação");

        boolean cliente = ref.getIdCliente().equals(idUsuario);
        Avaliacao a = new Avaliacao();
        a.setIdContratacao(req.idContratacao());
        a.setIdUsuarioAvaliador(idUsuario);
        a.setIdUsuarioAvaliado(cliente ? ref.getIdPrestador() : ref.getIdCliente());
        a.setPapelAvaliado(cliente ? "PRESTADOR" : "CLIENTE");
        a.setNota(req.nota());
        a.setComentario(req.comentario() == null || req.comentario().isBlank() ? null : req.comentario().strip());
        a.setDataHora(LocalDateTime.now());
        avaliacoes.save(a);

        publicarReputacao(a.getIdUsuarioAvaliado(), a.getPapelAvaliado());
        return AvaliacaoResponse.criar(a);
    }

    public List<AvaliacaoResponse> daContratacao(Long idContratacao, Long idUsuario) {
        exigirParticipante(ref(idContratacao), idUsuario);
        return avaliacoes.findByIdContratacaoAndOcultaFalse(idContratacao)
                .stream().map(AvaliacaoResponse::criar).toList();
    }

    public ReputacaoResponse reputacao(Long idUsuario, String papel) {
        exigirPapel(papel);
        ResumoNota r = avaliacoes.resumo(idUsuario, papel);
        long total = r.total() == null ? 0 : r.total();
        BigDecimal media = (total == 0 || r.media() == null) ? null
                : BigDecimal.valueOf(r.media()).setScale(2, RoundingMode.HALF_UP);
        return new ReputacaoResponse(idUsuario, papel, media, total);
    }

    public List<AvaliacaoResponse> doUsuario(Long idUsuario, String papel, int pagina) {
        exigirPapel(papel);
        return avaliacoes.findByIdUsuarioAvaliadoAndPapelAvaliadoAndOcultaFalseOrderByIdAvaliacaoDesc(
                        idUsuario, papel, PageRequest.of(Math.max(pagina, 0), 10))
                .stream().map(AvaliacaoResponse::criar).toList();
    }

    // ---- RF18: moderação ----
    public List<AvaliacaoResponse> recentes() {
        return avaliacoes.findTop50ByOrderByIdAvaliacaoDesc().stream().map(AvaliacaoResponse::criar).toList();
    }

    @Transactional
    public void moderar(Long idAvaliacao, Long idAdmin, boolean ocultar) {
        Avaliacao a = avaliacoes.findById(idAvaliacao)
                .orElseThrow(() -> erro(HttpStatus.NOT_FOUND, "Avaliação não encontrada"));
        if (a.isOculta() == ocultar) return;
        a.setOculta(ocultar);
        a.setIdAdminModerador(idAdmin);
        a.setDataModeracao(LocalDateTime.now());
        avaliacoes.save(a);
        publicarReputacao(a.getIdUsuarioAvaliado(), a.getPapelAvaliado());
    }

    // ---- auxiliares ----
    private void publicarReputacao(Long idUsuario, String papel) {
        ReputacaoResponse r = reputacao(idUsuario, papel);   // a query faz flush do save acima
        events.publishEvent(new ReputacaoAtualizada(idUsuario, papel, r.media(), r.total()));
    }

    private ContratacaoRef ref(Long id) {
        return refs.findById(id).orElseThrow(() -> erro(HttpStatus.NOT_FOUND, "Contratação não encontrada"));
    }

    private void exigirParticipante(ContratacaoRef ref, Long idUsuario) {
        if (!ref.getIdCliente().equals(idUsuario) && !ref.getIdPrestador().equals(idUsuario))
            throw erro(HttpStatus.FORBIDDEN, "Você não participa desta contratação");
    }

    private void exigirPapel(String papel) {
        if (!"PRESTADOR".equals(papel) && !"CLIENTE".equals(papel))
            throw erro(HttpStatus.BAD_REQUEST, "papel deve ser PRESTADOR ou CLIENTE");
    }

    private ResponseStatusException erro(HttpStatus status, String msg) {
        return new ResponseStatusException(status, msg);
    }
}