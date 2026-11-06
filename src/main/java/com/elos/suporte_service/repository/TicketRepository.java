package com.elos.suporte_service.repository;

import com.elos.suporte_service.suporte.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByIdUsuarioOrderByIdTicketDesc(Long idUsuario);
    List<Ticket> findByStatusInOrderByIdTicketAsc(Collection<String> status);
}