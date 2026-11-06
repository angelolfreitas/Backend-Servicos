package com.elos.catalogo_service;

import com.elos.catalogo_service.catalogo.ReputacaoPrestador;
import com.elos.catalogo_service.dto.catalogo.ReputacaoAtualizada;
import com.elos.catalogo_service.repository.ReputacaoPrestadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReputacaoListener {
    private final ReputacaoPrestadorRepository repository;

    @KafkaListener(topics = "reputacao-atualizada", groupId = "catalogo",
            properties = "spring.json.value.default.type=com.elos.catalogo_service.dto.catalogo.ReputacaoAtualizada")
    public void ouvir(ReputacaoAtualizada e) {
        if (!"PRESTADOR".equals(e.papel())) return;   // a vitrine só usa a nota como prestador
        repository.save(new ReputacaoPrestador(e.idUsuario(), e.media(), e.total()));
    }
}