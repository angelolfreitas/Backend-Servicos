package com.elos.catalogo_service.dto.catalogo;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;


public record ServicoRequest(
        @NotNull(message = "Informe o tipo de serviço") Long idTipoServico,
        @NotBlank(message = "Descrição obrigatória") @Size(max = 255, message = "Máximo de 255 caracteres") String descricao,
        @NotNull(message = "Informe o valor") @Positive(message = "Valor deve ser maior que zero")
        @Digits(integer = 8, fraction = 2, message = "Valor inválido") BigDecimal valor,
        @NotNull(message = "Informe o status")
        @Pattern(regexp = "ATIVO|INATIVO", message = "status deve ser ATIVO ou INATIVO") String status) {}
