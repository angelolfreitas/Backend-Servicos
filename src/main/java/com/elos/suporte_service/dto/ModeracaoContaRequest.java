package com.elos.suporte_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ModeracaoContaRequest(
        @NotNull(message = "Informe o usuário") Long idUsuario,
        @NotNull(message = "Informe a ação")
        @Pattern(regexp = "SUSPENDER|BANIR|REATIVAR", message = "Ação deve ser SUSPENDER, BANIR ou REATIVAR")
        String acao,
        @NotBlank(message = "Informe o motivo") @Size(max = 500) String motivo,
        Long idTicket) {}
