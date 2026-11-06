package com.elos.dashboard_service.repository;

import com.elos.dashboard_service.dashboard.ContratacaoStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContratacaoStatusRepository extends JpaRepository<ContratacaoStatus, Long> {
    long countByStatus(String status);
}

