package com.elos.reputacao_service.dto;

import com.elos.reputacao_service.reputacao.Avaliacao;

import java.time.LocalDateTime;

public record AvaliacaoResponse(Long id, Long idContratacao, Long idAvaliador, Long idAvaliado,
                                String papelAvaliado, Integer nota, String comentario,
                                LocalDateTime dataHora, boolean oculta) {
    public static AvaliacaoResponse criar(Avaliacao a) {
        return new AvaliacaoResponse(a.getIdAvaliacao(), a.getIdContratacao(), a.getIdUsuarioAvaliador(),
                a.getIdUsuarioAvaliado(), a.getPapelAvaliado(), a.getNota(), a.getComentario(),
                a.getDataHora(), a.isOculta());
    }
}