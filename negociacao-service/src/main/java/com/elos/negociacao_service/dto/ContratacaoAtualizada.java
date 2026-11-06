package com.elos.negociacao_service.dto;

import java.math.BigDecimal;

public record ContratacaoAtualizada(Long idContratacao, Long idCliente, Long idPrestador,
                                    Long idServico, String status, BigDecimal valor) {}