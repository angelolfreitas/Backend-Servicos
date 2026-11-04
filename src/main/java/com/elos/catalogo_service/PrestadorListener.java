package com.elos.catalogo_service;

import com.elos.catalogo_service.catalogo.PrestadorHabilitado;
import com.elos.catalogo_service.dto.catalogo.PrestadorAprovado;
import com.elos.catalogo_service.dto.catalogo.PrestadorRejeitado;
import com.elos.catalogo_service.dto.catalogo.ServicoAtualizado;
import com.elos.catalogo_service.repository.PrestadorHabilitadoRepository;
import com.elos.catalogo_service.repository.ServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PrestadorListener {
    private final PrestadorHabilitadoRepository repository;
    private final ServicoRepository servicoRepository;
    private final EventPublisher events;

    @KafkaListener(topics = "prestador-aprovado", groupId = "catalogo")
    public void ouvir(PrestadorAprovado evento) {
        repository.save(new PrestadorHabilitado(evento.idPrestador(), evento.idUsuario()));
    }

    @KafkaListener(topics = "prestador-rejeitado", groupId = "catalogo",
            properties = "spring.json.value.default.type=com.elos.catalogo_service.dto.catalogo.PrestadorRejeitado")
    @Transactional
    public void rejeitado(PrestadorRejeitado evento) {
        repository.deleteById(evento.idPrestador());
        servicoRepository.findByIdPrestador(evento.idPrestador()).forEach(s -> {
            s.setStatus("SUSPENSO");
            servicoRepository.save(s);
            events.servicoAtualizado(new ServicoAtualizado(
                    s.getIdServico(), evento.idUsuario(), s.getValor(), "SUSPENSO"));
        });
    }
}