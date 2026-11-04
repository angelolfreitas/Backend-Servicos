package com.elos.catalogo_service.dto.catalogo;

import com.elos.catalogo_service.catalogo.Categoria;

public record CategoriaResponse(Long id, String nome, String statusAtivo) {
    public static CategoriaResponse criar(Categoria c) {
        return new CategoriaResponse(c.getIdCategoria(), c.getNome(), c.getStatusAtivo());
    }
}
