package com.elos.suporte_service;

import com.elos.suporte_service.dto.AvaliacaoModerada;
import com.elos.suporte_service.dto.ContaModerada;
import com.elos.suporte_service.dto.TicketAtualizado;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class EventPublisher {
    private final KafkaTemplate<String, Object> kafka;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void conta(ContaModerada e) {
        kafka.send("conta-moderada", e.idUsuario().toString(), e);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void avaliacao(AvaliacaoModerada e) {
        kafka.send("avaliacao-moderada", e.idAvaliacao().toString(), e);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void ticket(TicketAtualizado e) {
        kafka.send("ticket-atualizado", e.idTicket().toString(), e);
    }

}