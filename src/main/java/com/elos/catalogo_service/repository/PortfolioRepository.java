package com.elos.catalogo_service.repository;

import com.elos.catalogo_service.catalogo.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    Optional<Portfolio> findByIdPrestador(Long idPrestador);
}