package com.elos.catalogo_service.repository;

import com.elos.catalogo_service.catalogo.Portfolio;
import com.elos.catalogo_service.dto.catalogo.VitrineItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    Optional<Portfolio> findByIdPrestador(Long idPrestador);
    @Query(VitrineQueries.BASE + " order by coalesce(r.media, 0) desc, coalesce(r.total, 0) desc, p.idPrestador")
    List<VitrineItem> buscarPorNota(@Param("texto") String texto, @Param("semTexto") boolean semTexto,
                                    @Param("idCategoria") Long idCategoria,
                                    @Param("notaMinima") BigDecimal notaMinima, Pageable pageable);

    @Query(VitrineQueries.BASE + " order by p.idPortfolio desc")
    List<VitrineItem> buscarRecentes(@Param("texto") String texto, @Param("semTexto") boolean semTexto,
                                     @Param("idCategoria") Long idCategoria,
                                     @Param("notaMinima") BigDecimal notaMinima, Pageable pageable);
}