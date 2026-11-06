package com.elos.dashboard_service.repository;

import com.elos.dashboard_service.dashboard.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TicketStatusRepository extends JpaRepository<TicketStatus, Long> {

    // "em aberto" = tudo que não foi encerrado. Ajuste o texto se o suporte usar outro nome (ex.: FECHADO).
    @Query("select count(t) from TicketStatus t where t.status <> 'ENCERRADO'")
    long contarAbertos();
}