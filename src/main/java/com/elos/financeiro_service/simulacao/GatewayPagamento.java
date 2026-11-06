package com.elos.financeiro_service.simulacao;

import java.math.BigDecimal;

public interface GatewayPagamento {
    void cobrar(BigDecimal valor, String formaPagamento, String token);
    void repassar(Long idPrestador, BigDecimal valor);
    void reembolsar(Long idCliente, BigDecimal valor);
}
