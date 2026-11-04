package com.elos.catalogo_service.dto.catalogo;

import java.math.BigDecimal;

public record ServicoAtualizado(Long idServico, Long idUsuarioPrestador, BigDecimal valor, String status) {}
