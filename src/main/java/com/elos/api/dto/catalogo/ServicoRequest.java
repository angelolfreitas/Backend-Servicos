package com.elos.api.dto.catalogo;

import java.math.BigDecimal;

public record ServicoRequest(Long idTipoServico,
                             Long idPrestador,
                             String descricao,
                             BigDecimal valor,
                             String status) {
}
