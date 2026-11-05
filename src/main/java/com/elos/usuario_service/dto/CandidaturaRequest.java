package com.elos.usuario_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CandidaturaRequest(
        @NotNull(message = "Informe PF ou PJ")
        @Pattern(regexp = "PF|PJ", message = "tipoPrestador deve ser PF ou PJ")
        String tipoPrestador,

        @Pattern(regexp = "\\d{11}", message = "CPF deve ter 11 dígitos")
        String cpf,

        @Pattern(regexp = "\\d{14}", message = "CNPJ deve ter 14 dígitos")
        String cnpj) {}