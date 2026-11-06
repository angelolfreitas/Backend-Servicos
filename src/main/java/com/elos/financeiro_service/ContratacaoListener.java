package com.elos.financeiro_service;

import com.elos.financeiro_service.dto.ContratacaoAtualizada;
import com.elos.financeiro_service.financeiro.ContratacaoRef;
import com.elos.financeiro_service.repository.ContratacaoRefRepository;
import com.elos.financeiro_service.service.PagamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContratacaoListener {
    private final ContratacaoRefRepository refs;
    private final PagamentoService pagamentos;

    @KafkaListener(topics = "contratacao-atualizada", groupId = "financeiro")
    public void ouvir(ContratacaoAtualizada e) {
        refs.save(new ContratacaoRef(e.idContratacao(), e.idCliente(), e.idPrestador(), e.valor(), e.status()));
        if ("CONCLUIDA".equals(e.status())) pagamentos.aoConcluir(e.idContratacao());
        if ("CANCELADA".equals(e.status())) pagamentos.aoCancelar(e.idContratacao());
    }
}