package com.elos.catalogo_service.service;

import com.elos.catalogo_service.dto.catalogo.VitrineItem;
import com.elos.catalogo_service.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class VitrineService {
    private final PortfolioRepository portfolios;

    public List<VitrineItem> buscar(String texto, Long idCategoria, BigDecimal notaMinima,
                                    String ordem, int pagina) {
        String t = texto == null ? "" : texto.strip().toLowerCase(Locale.ROOT);
        if (t.length() > 100)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Busca com no máximo 100 caracteres");
        BigDecimal nota = notaMinima == null ? BigDecimal.ZERO : notaMinima;
        if (nota.signum() < 0 || nota.compareTo(BigDecimal.valueOf(5)) > 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "notaMinima deve ficar entre 0 e 5");
        Long categoria = idCategoria == null ? 0L : idCategoria;
        Pageable page = PageRequest.of(Math.max(pagina, 0), 12);

        return switch (ordem) {
            case "nota" -> portfolios.buscarPorNota(t, t.isEmpty(), categoria, nota, page);
            case "recentes" -> portfolios.buscarRecentes(t, t.isEmpty(), categoria, nota, page);
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ordem deve ser nota ou recentes");
        };
    }
}