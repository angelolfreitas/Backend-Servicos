package com.elos.suporte_service.dto;

import com.elos.suporte_service.suporte.Moderacao;

import java.time.LocalDateTime;

public record ModeracaoResponse(Long id, Long idAdministrador, Long idUsuario, Long idAvaliacao,
                                String tipoAcao, String motivo, Long idTicket, LocalDateTime dataHora) {
    public static ModeracaoResponse criar(Moderacao m) {
        return new ModeracaoResponse(m.getIdModeracao(), m.getIdAdministrador(), m.getIdUsuario(),
                m.getIdAvaliacao(), m.getTipoAcao(), m.getMotivo(), m.getIdTicket(), m.getDataHora());
    }
}