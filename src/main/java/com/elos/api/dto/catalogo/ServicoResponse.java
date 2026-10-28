package com.elos.api.dto.catalogo;

import com.elos.api.entity.catalogo.Servico;

import java.math.BigDecimal;

public record ServicoResponse(Long id,
                              String descricao,
                              BigDecimal valor,
                              String status) {
    public static ServicoResponse criar(Servico servico) {
        return new ServicoResponse(
                servico.getIdServico(),
                servico.getDescricao(),
                servico.getValor(),
                servico.getStatus());
    }
}
