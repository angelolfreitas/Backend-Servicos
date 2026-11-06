package com.elos.suporte_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ModeracaoAvaliacaoRequest(
        @NotNull(message = "Informe a avaliação") Long idAvaliacao,
        @NotNull(message = "Informe ocultar true ou false") Boolean ocultar,
        @NotBlank(message = "Informe o motivo") @Size(max = 500) String motivo,
        Long idTicket) {}