package com.elos.financeiro_service;


import com.elos.financeiro_service.dto.PagamentoLiberado;
import com.elos.financeiro_service.dto.PagamentoRetido;
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
    public void retido(PagamentoRetido e) {
        kafka.send("pagamento-retido", e.idContratacao().toString(), e);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void liberado(PagamentoLiberado e) {
        kafka.send("pagamento-liberado", e.idContratacao().toString(), e);
    }
}
