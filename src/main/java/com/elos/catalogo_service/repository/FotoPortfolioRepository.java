package com.elos.catalogo_service.repository;

import com.elos.catalogo_service.catalogo.FotoPortfolio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FotoPortfolioRepository extends JpaRepository<FotoPortfolio, Long> {
    List<FotoPortfolio> findByIdPortfolio(Long idPortfolio);
}