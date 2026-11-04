package com.elos.negociacao_service.dto;

public record ContratacaoAtualizada(Long idContratacao, Long idCliente, Long idPrestador,
                                    Long idServico, String status) {}