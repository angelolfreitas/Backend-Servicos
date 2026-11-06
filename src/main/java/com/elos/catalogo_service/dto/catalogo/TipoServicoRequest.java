package com.elos.catalogo_service.dto.catalogo;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TipoServicoRequest(
        @NotBlank(message = "Nome obrigatório") @Size(max = 60, message = "Máximo de 60 caracteres") String nome) {}
