package com.elos.dashboard_service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PagamentoLiberado(Long idContratacao, Long idPrestador, BigDecimal valorTotal,
                                BigDecimal valorComissao, BigDecimal valorLiquido, LocalDate data) {}

