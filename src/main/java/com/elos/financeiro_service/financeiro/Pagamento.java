package com.elos.financeiro_service.financeiro;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagamento")
@Getter
@Setter
@NoArgsConstructor
public class Pagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pagamento")
    private Long idPagamento;
    @Column(name = "id_contratacao", nullable = false, unique = true)
    private Long idContratacao;
    @Column(name = "id_cliente", nullable = false)
    private Long idCliente;
    @Column(name = "id_prestador", nullable = false)
    private Long idPrestador;
    @Column(name = "valor_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorTotal;
    @Column(name = "valor_liquido", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorLiquido;
    @Column(name = "forma_pagamento", nullable = false, length = 10)
    private String formaPagamento;
    @Column(name = "status", nullable = false, length = 25)
    private String status;
    @Column(name = "data", nullable = false)
    private LocalDateTime data;
}