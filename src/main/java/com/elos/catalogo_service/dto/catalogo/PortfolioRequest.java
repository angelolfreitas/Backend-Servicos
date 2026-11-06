package com.elos.catalogo_service.dto.catalogo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PortfolioRequest(
        @Size(max = 255) String fotoPerfil,
        @Size(max = 255, message = "Currículo com no máximo 255 caracteres") String curriculo,
        @NotBlank(message = "Cidade obrigatória") @Size(max = 80) String cidade,
        @NotBlank(message = "Região obrigatória") @Size(max = 40) String regiao,
        @Size(max = 255, message = "Descrição com no máximo 255 caracteres") String descricao) {}
