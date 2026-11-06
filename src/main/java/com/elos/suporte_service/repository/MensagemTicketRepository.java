package com.elos.suporte_service.repository;

import com.elos.suporte_service.suporte.MensagemTicket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MensagemTicketRepository extends JpaRepository<MensagemTicket, Long> {
    List<MensagemTicket> findByIdTicketOrderByDataHoraAscIdMensagemTicketAsc(Long idTicket);
}
