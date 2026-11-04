package com.elos.catalogo_service.dto.catalogo;

import com.elos.catalogo_service.catalogo.TipoServico;

public record TipoServicoResponse(Long id, Long idCategoria, String nome, String statusAtivo) {
    public static TipoServicoResponse criar(TipoServico t) {
        return new TipoServicoResponse(t.getIdTipoServico(), t.getIdCategoria(), t.getNome(), t.getStatusAtivo());
    }
}
