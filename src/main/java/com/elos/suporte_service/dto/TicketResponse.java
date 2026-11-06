package com.elos.suporte_service.dto;

import java.time.LocalDateTime;
import java.util.List;

public record TicketResponse(Long id, String protocolo, Long idUsuario, Long idAdministrador,
                             String categoriaProblema, Long idContratacao, String status,
                             LocalDateTime dataAbertura, LocalDateTime dataFechamento,
                             List<MensagemTicketResponse> mensagens) {}