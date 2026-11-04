package com.elos.negociacao_service.dto;

import java.math.BigDecimal;

public record ServicoAtualizado(Long idServico, Long idUsuarioPrestador, BigDecimal valor, String status) {}