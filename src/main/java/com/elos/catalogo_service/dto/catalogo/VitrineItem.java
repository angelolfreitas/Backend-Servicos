package com.elos.catalogo_service.dto.catalogo;

import java.math.BigDecimal;

public record VitrineItem(Long idPrestador, String fotoPerfil, String cidade, String regiao,
                          String descricao, BigDecimal media, Long total,
                          BigDecimal menorValor, Long qtdServicos) {}