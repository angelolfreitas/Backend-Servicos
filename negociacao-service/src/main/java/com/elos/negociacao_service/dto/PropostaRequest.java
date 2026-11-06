package com.elos.negociacao_service.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PropostaRequest(
        @NotNull(message = "Informe o serviço") Long idServico,
        @Positive(message = "Valor deve ser maior que zero") @Digits(integer = 8, fraction = 2) BigDecimal valor,
        @FutureOrPresent(message = "Data de início no passado") LocalDate dataInicio,
        @FutureOrPresent(message = "Validade no passado") LocalDate validade,
        @Size(max = 255, message = "Máximo de 255 caracteres") String descricao) {}