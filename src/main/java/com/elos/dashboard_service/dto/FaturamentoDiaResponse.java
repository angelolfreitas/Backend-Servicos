package com.elos.dashboard_service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FaturamentoDiaResponse(LocalDate data, BigDecimal valor, long quantidade) {}