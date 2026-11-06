package com.elos.dashboard_service;

import com.elos.dashboard_service.dashboard.ContratacaoStatus;
import com.elos.dashboard_service.dto.ContratacaoAtualizada;
import com.elos.dashboard_service.repository.ContratacaoStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContratacaoListener {
    private final ContratacaoStatusRepository repository;

    @KafkaListener(topics = "contratacao-atualizada", groupId = "dashboard",
            properties = "spring.json.value.default.type=com.elos.dashboard_service.dto.ContratacaoAtualizada")
    public void ouvir(ContratacaoAtualizada e) {
        repository.save(new ContratacaoStatus(e.idContratacao(), e.status()));
    }
}