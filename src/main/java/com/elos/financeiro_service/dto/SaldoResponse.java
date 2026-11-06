package com.elos.financeiro_service.dto;

import java.math.BigDecimal;

public record SaldoResponse(BigDecimal liberado, BigDecimal aReceber) {}