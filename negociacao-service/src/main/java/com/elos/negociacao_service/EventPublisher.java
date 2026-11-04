package com.elos.negociacao_service;

import com.elos.negociacao_service.dto.ContratacaoAtualizada;
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
    public void aoCommitar(ContratacaoAtualizada evento) {
        kafka.send("contratacao-atualizada", evento.idContratacao().toString(), evento);
    }
}