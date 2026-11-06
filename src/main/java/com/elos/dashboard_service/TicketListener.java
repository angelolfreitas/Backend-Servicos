package com.elos.dashboard_service;


import com.elos.dashboard_service.dashboard.TicketStatus;
import com.elos.dashboard_service.dto.TicketAtualizado;
import com.elos.dashboard_service.repository.TicketStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketListener {
    private final TicketStatusRepository repository;

    @KafkaListener(topics = "ticket-atualizado", groupId = "dashboard",
            properties = "spring.json.value.default.type=com.elos.dashboard_service.dto.TicketAtualizado")
    public void ouvir(TicketAtualizado e) {
        repository.save(new TicketStatus(e.idTicket(), e.status()));
    }
}