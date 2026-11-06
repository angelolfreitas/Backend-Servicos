package com.elos.reputacao_service;

import com.elos.reputacao_service.dto.AvaliacaoModerada;
import com.elos.reputacao_service.service.AvaliacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ModeracaoListener {
    private final AvaliacaoService service;

    @KafkaListener(topics = "avaliacao-moderada", groupId = "reputacao",
            properties = "spring.json.value.default.type=com.elos.reputacao_service.dto.AvaliacaoModerada")
    public void ouvir(AvaliacaoModerada e) {
        service.moderar(e.idAvaliacao(), e.idAdmin(), e.ocultar());
    }
}