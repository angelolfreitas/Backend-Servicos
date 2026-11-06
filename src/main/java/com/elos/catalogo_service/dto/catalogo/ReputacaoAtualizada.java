package com.elos.catalogo_service.dto.catalogo;

import java.math.BigDecimal;

public record ReputacaoAtualizada(Long idUsuario, String papel, BigDecimal media, long total) {}
