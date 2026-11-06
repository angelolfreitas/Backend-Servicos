package com.elos.financeiro_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EtapaResponse(Integer numero, BigDecimal percentual, BigDecimal valor,
                            String status, LocalDateTime dataLiberacao) {}