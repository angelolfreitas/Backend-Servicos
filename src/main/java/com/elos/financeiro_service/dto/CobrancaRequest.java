package com.elos.financeiro_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CobrancaRequest(
        @NotNull(message = "Informe a forma de pagamento")
        @Pattern(regexp = "PIX|CARTAO", message = "formaPagamento deve ser PIX ou CARTAO") String formaPagamento,
        @NotBlank(message = "Informe o token do pagamento") String tokenPagamento) {}