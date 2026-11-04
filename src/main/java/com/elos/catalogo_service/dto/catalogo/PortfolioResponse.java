package com.elos.catalogo_service.dto.catalogo;

import java.util.List;

public record PortfolioResponse(Long idPrestador, String fotoPerfil, String curriculo,
                                String cidade, String regiao, String descricao,
                                List<FotoResponse> fotos) {}
