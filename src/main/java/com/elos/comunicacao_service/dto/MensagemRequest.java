package com.elos.comunicacao_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MensagemRequest(
        @NotBlank(message = "Mensagem vazia") @Size(max = 1000, message = "Máximo de 1000 caracteres") String texto) {}