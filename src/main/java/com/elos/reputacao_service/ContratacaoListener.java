package com.elos.reputacao_service;

import com.elos.reputacao_service.dto.ContratacaoAtualizada;
import com.elos.reputacao_service.repository.ContratacaoRefRepository;
import com.elos.reputacao_service.reputacao.ContratacaoRef;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContratacaoListener {
    private final ContratacaoRefRepository repository;

    @KafkaListener(topics = "contratacao-atualizada", groupId = "reputacao")
    public void ouvir(ContratacaoAtualizada e) {
        repository.save(new ContratacaoRef(e.idContratacao(), e.idCliente(), e.idPrestador(), e.status()));
    }
}