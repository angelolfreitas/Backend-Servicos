package com.elos.suporte_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record TicketRequest(
        @NotNull(message = "Informe a categoria")
        @Pattern(regexp = "DUVIDA_TECNICA|DENUNCIA_CONDUTA|DESACORDO_CONTRATUAL|OUTRO",
                message = "Categoria inválida")
        String categoriaProblema,
        @NotBlank(message = "Descreva o problema")
        @Size(max = 2000, message = "Máximo de 2000 caracteres") String mensagem,
        Long idContratacao) {}