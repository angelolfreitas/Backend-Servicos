package com.elos.negociacao_service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PropostaRequest(Long idServico, BigDecimal valor, LocalDate dataInicio,
                              LocalDate validade, String descricao) {}