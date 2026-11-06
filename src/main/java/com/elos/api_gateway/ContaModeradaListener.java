package com.elos.api_gateway;

import com.elos.api_gateway.dto.ContaModerada;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContaModeradaListener {
    private final ContasBloqueadas bloqueadas;

    // Grupo novo a cada subida + earliest: o gateway relê o tópico inteiro e reconstrói a lista sozinho.
    @KafkaListener(topics = "conta-moderada", groupId = "gateway-${random.uuid}",
            properties = "spring.json.value.default.type=com.elos.api_gateway.dto.ContaModerada")
    public void ouvir(ContaModerada e) {
        bloqueadas.aplicar(e.idUsuario(), e.acao());
    }
}