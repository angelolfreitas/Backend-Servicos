package com.elos.api.repository.suporte;

import com.elos.api.entity.suporte.MensagemTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MensagemTicketRepository extends JpaRepository<MensagemTicket, Long> {
}
