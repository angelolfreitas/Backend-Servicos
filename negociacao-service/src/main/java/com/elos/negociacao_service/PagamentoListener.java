package com.elos.negociacao_service;


import com.elos.negociacao_service.dto.PagamentoRetido;
import com.elos.negociacao_service.service.NegociacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PagamentoListener {
    private final NegociacaoService service;

    @KafkaListener(topics = "pagamento-retido", groupId = "negociacao",
            properties = "spring.json.value.default.type=com.elos.negociacao_service.dto.PagamentoRetido")
    public void ouvir(PagamentoRetido e) {
        service.iniciarExecucao(e.idContratacao());
    }
}