package com.elos.dashboard_service;

import com.elos.dashboard_service.dashboard.ComissaoLiberada;
import com.elos.dashboard_service.dto.PagamentoLiberado;
import com.elos.dashboard_service.repository.ComissaoLiberadaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PagamentoListener {
    private final ComissaoLiberadaRepository repository;

    @KafkaListener(topics = "pagamento-liberado", groupId = "dashboard",
            properties = "spring.json.value.default.type=com.elos.dashboard_service.dto.PagamentoLiberado")
    public void ouvir(PagamentoLiberado e) {
        repository.save(new ComissaoLiberada(e.idContratacao(), e.data(), e.valorTotal(), e.valorComissao()));
    }
}