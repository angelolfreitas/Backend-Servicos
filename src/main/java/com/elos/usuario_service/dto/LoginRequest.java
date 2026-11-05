package com.elos.usuario_service.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "E-mail obrigatório") String email,
        @NotBlank(message = "Senha obrigatória") String password) {}