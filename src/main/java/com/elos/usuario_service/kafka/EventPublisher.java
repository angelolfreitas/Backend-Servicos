package com.elos.usuario_service.kafka;

import com.elos.usuario_service.dto.PrestadorAprovado;
import com.elos.usuario_service.dto.PrestadorRejeitado;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
/// Producer deste microsservice.
///  envia uma aprovacao para o tópico de aprovacao e rejeicao de prestadores, no microsservice de catálogo.
@Component
@RequiredArgsConstructor
public class EventPublisher {
    private final KafkaTemplate<String, Object> kafka;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void aprovado(PrestadorAprovado e) {
        kafka.send("prestador-aprovado", e.idPrestador().toString(), e);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void rejeitado(PrestadorRejeitado e) {
        kafka.send("prestador-rejeitado", e.idPrestador().toString(), e);
    }
}