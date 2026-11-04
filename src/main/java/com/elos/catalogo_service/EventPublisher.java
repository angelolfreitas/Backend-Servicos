package com.elos.catalogo_service;

import com.elos.catalogo_service.dto.catalogo.ServicoAtualizado;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventPublisher {
    private final KafkaTemplate<String, Object> kafka;

    public void servicoAtualizado(ServicoAtualizado evento) {
        kafka.send("servico-atualizado", evento.idServico().toString(), evento);
    }
}