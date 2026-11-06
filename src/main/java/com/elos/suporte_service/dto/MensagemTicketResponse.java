package com.elos.suporte_service.dto;


import java.time.LocalDateTime;

public record MensagemTicketResponse(Long id, Long idRemetente, String autor,
                                     String mensagem, LocalDateTime dataHora) {}