package com.elos.negociacao_service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ContratacaoResponse(Long id, Long idCliente, Long idPrestador, Long idServico,
                                  Long idProposta, BigDecimal valor, String descricao,
                                  LocalDate dataInicio, LocalDate dataFinalizacaoPrevista,
                                  LocalDate dataFinalizacao, String status) {}
