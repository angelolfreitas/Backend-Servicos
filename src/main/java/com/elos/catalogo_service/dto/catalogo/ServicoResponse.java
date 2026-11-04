package com.elos.catalogo_service.dto.catalogo;



import com.elos.catalogo_service.catalogo.Servico;

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
