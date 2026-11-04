package com.elos.catalogo_service.dto.catalogo;

import java.math.BigDecimal;

public record ServicoRequest(Long idTipoServico, String descricao, BigDecimal valor, String status) {}