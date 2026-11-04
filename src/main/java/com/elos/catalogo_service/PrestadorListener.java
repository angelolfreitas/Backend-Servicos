package com.elos.catalogo_service;

import com.elos.catalogo_service.catalogo.PrestadorHabilitado;
import com.elos.catalogo_service.dto.catalogo.PrestadorAprovado;
import com.elos.catalogo_service.repository.PrestadorHabilitadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
@Component
@RequiredArgsConstructor
public class PrestadorListener {
    private final PrestadorHabilitadoRepository repository;

    @KafkaListener(topics = "prestador-aprovado", groupId = "catalogo")
    public void ouvir(PrestadorAprovado evento) {
        repository.save(new PrestadorHabilitado(evento.idPrestador(), evento.idUsuario()));
    }
}