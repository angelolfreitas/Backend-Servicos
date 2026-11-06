package com.elos.financeiro_service.simulacao;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Component
@Slf4j
public class GatewaySimulado implements GatewayPagamento {
    @Override
    public void cobrar(BigDecimal valor, String formaPagamento, String token) {
        if ("tok_falha".equals(token))
            throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED, "Pagamento recusado");
        log.info("[SIMULADO] cobrança de {} via {}", valor, formaPagamento);
    }
    @Override
    public void repassar(Long idPrestador, BigDecimal valor) {
        log.info("[SIMULADO] repasse de {} ao prestador {}", valor, idPrestador);
    }
    @Override
    public void reembolsar(Long idCliente, BigDecimal valor) {
        log.info("[SIMULADO] reembolso de {} ao cliente {}", valor, idCliente);
    }
}