package com.elos.suporte_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record MensagemTicketRequest(
        @NotBlank(message = "Mensagem vazia")
        @Size(max = 2000, message = "Máximo de 2000 caracteres") String mensagem) {}

