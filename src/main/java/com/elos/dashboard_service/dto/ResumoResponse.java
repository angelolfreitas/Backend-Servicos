package com.elos.dashboard_service.dto;

import java.math.BigDecimal;

public record ResumoResponse(long totalContratacoes, long contratacoesFinalizadas, BigDecimal taxaFinalizacao,
                             long ticketsAbertos, BigDecimal faturamentoHoje, BigDecimal faturamentoTotal) {}
