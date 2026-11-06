package com.elos.reputacao_service.dto;

public record ContratacaoAtualizada(Long idContratacao, Long idCliente, Long idPrestador,
                                    Long idServico, String status) {}