package com.elos.comunicacao_service;

import com.elos.comunicacao_service.chat.ContratacaoRef;
import com.elos.comunicacao_service.dto.ContratacaoAtualizada;
import com.elos.comunicacao_service.repository.ContratacaoRefRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContratacaoListener {
    private final ContratacaoRefRepository repository;

    @KafkaListener(topics = "contratacao-atualizada", groupId = "comunicacao")
    public void ouvir(ContratacaoAtualizada e) {
        repository.save(new ContratacaoRef(e.idContratacao(), e.idCliente(), e.idPrestador(), e.status()));
    }
}