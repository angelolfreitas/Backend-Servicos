package com.elos.usuario_service;

import com.elos.usuario_service.dto.PrestadorAprovado;
import com.elos.usuario_service.repository.PrestadorRepository;
import com.elos.usuario_service.usuario.Prestador;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PrestadorService {
    private final PrestadorRepository repository;
    private final EventPublisher events;

    @Transactional
    public void aprovar(Long id) {
        Prestador p = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        p.setStatusAprovacao("APROVADO");
        repository.save(p);
        events.prestadorAprovado(new PrestadorAprovado(p.getIdPrestador(), p.getIdUsuario()));
    }
}