package com.elos.negociacao_service;

import com.elos.negociacao_service.dto.ServicoAtualizado;
import com.elos.negociacao_service.negociacao.ServicoRef;
import com.elos.negociacao_service.repository.ServicoRefRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ServicoListener {
    private final ServicoRefRepository repository;

    @KafkaListener(topics = "servico-atualizado", groupId = "negociacao")
    public void ouvir(ServicoAtualizado evento) {
        repository.save(new ServicoRef(evento.idServico(), evento.idUsuarioPrestador(),
                evento.valor(), evento.status()));
    }
}