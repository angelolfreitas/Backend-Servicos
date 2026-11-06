package com.elos.reputacao_service.dto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AvaliacaoRequest(
        @NotNull(message = "Informe a contratação") Long idContratacao,
        @NotNull(message = "Informe a nota")
        @Min(value = 1, message = "A nota mínima é 1")
        @Max(value = 5, message = "A nota máxima é 5") Integer nota,
        @Size(max = 500, message = "Comentário com no máximo 500 caracteres") String comentario) {}
