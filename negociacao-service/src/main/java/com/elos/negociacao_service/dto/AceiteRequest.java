package com.elos.negociacao_service.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AceiteRequest(
        @NotNull(message = "Informe o prazo de entrega")
        @FutureOrPresent(message = "Prazo no passado") LocalDate dataFinalizacaoPrevista) {}
