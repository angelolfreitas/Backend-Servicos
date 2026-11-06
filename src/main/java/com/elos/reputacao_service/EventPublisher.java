package com.elos.reputacao_service;

import com.elos.reputacao_service.dto.ReputacaoAtualizada;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class EventPublisher {
    private final KafkaTemplate<String, Object> kafka;

    // a chave é o usuário: todas as atualizações dele vão para a mesma partição e chegam em ordem
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void reputacao(ReputacaoAtualizada e) {
        kafka.send("reputacao-atualizada", e.idUsuario().toString(), e);
    }
}