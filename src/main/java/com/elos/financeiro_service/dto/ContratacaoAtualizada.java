package com.elos.financeiro_service.dto;

import java.math.BigDecimal;

public record ContratacaoAtualizada(Long idContratacao, Long idCliente, Long idPrestador,
                                    Long idServico, String status, BigDecimal valor) {}