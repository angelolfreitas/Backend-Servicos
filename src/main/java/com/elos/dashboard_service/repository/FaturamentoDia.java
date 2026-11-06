package com.elos.dashboard_service.repository;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface FaturamentoDia {
    LocalDate getData();
    BigDecimal getValor();
    Long getQtd();
}