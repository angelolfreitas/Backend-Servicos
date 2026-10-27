package com.elos.api.repository.portfolio;

import com.elos.api.entity.portfolio.FotoPortfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FotoPortfolioRepository extends JpaRepository<FotoPortfolio, Long> {
}
