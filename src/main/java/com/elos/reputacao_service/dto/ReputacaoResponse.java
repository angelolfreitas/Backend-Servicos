package com.elos.reputacao_service.dto;


import java.math.BigDecimal;

public record ReputacaoResponse(Long idUsuario, String papel, BigDecimal media, long total) {}
