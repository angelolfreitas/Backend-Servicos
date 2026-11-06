package com.elos.suporte_service.service;

import com.elos.suporte_service.dto.*;
import com.elos.suporte_service.repository.ModeracaoRepository;
import com.elos.suporte_service.suporte.Moderacao;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ModeracaoService {
    private final ModeracaoRepository moderacoes;
    private final ApplicationEventPublisher events;

    @Transactional
    public ModeracaoResponse moderarConta(Long idAdmin, ModeracaoContaRequest req) {
        if (req.idUsuario().equals(idAdmin))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Você não pode moderar a própria conta");
        String tipo = switch (req.acao()) {
            case "SUSPENDER" -> "SUSPENDER_CONTA";
            case "BANIR" -> "BANIR_CONTA";
            default -> "REATIVAR_CONTA";
        };
        String evento = switch (req.acao()) {
            case "SUSPENDER" -> "SUSPENSA";
            case "BANIR" -> "BANIDA";
            default -> "REATIVADA";
        };
        Moderacao m = nova(idAdmin, tipo, req.motivo(), req.idTicket());
        m.setIdUsuario(req.idUsuario());
        moderacoes.save(m);
        events.publishEvent(new ContaModerada(req.idUsuario(), evento, req.motivo()));
        return ModeracaoResponse.criar(m);
    }

    @Transactional
    public ModeracaoResponse moderarAvaliacao(Long idAdmin, ModeracaoAvaliacaoRequest req) {
        Moderacao m = nova(idAdmin, req.ocultar() ? "OCULTAR_AVALIACAO" : "EXIBIR_AVALIACAO",
                req.motivo(), req.idTicket());
        m.setIdAvaliacao(req.idAvaliacao());
        moderacoes.save(m);
        events.publishEvent(new AvaliacaoModerada(req.idAvaliacao(), req.ocultar(), idAdmin));
        return ModeracaoResponse.criar(m);
    }

    public List<ModeracaoResponse> recentes() {
        return moderacoes.findTop50ByOrderByIdModeracaoDesc().stream().map(ModeracaoResponse::criar).toList();
    }

    private Moderacao nova(Long idAdmin, String tipo, String motivo, Long idTicket) {
        Moderacao m = new Moderacao();
        m.setIdAdministrador(idAdmin);
        m.setTipoAcao(tipo);
        m.setMotivo(motivo.strip());
        m.setIdTicket(idTicket);
        m.setDataHora(LocalDateTime.now());
        return m;
    }
}