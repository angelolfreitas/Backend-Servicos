package com.elos.usuario_service;

import com.elos.usuario_service.dto.PrestadorAprovado;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventPublisher {
    private final KafkaTemplate<String, Object> kafka;

    public void prestadorAprovado(PrestadorAprovado evento) {
        kafka.send("prestador-aprovado", evento.idPrestador().toString(), evento);
    }
}