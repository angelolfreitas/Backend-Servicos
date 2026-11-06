package com.elos.financeiro_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PagamentoResponse(Long idContratacao, BigDecimal valorTotal, String formaPagamento, String status,
                                BigDecimal comissaoPercentual, BigDecimal valorComissao, BigDecimal valorLiquido,
                                LocalDateTime data, List<EtapaResponse> etapas) {}