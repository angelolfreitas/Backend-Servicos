package com.elos.financeiro_service.financeiro;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "etapa_pagamento")
@Getter
@Setter
@NoArgsConstructor
public class EtapaPagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_etapa_pagamento")
    private Long idEtapaPagamento;
    @Column(name = "id_pagamento", nullable = false)
    private Long idPagamento;
    @Column(name = "numero", nullable = false)
    private Integer numero;
    @Column(name = "percentual", nullable = false, precision = 5, scale = 2)
    private BigDecimal percentual;
    @Column(name = "valor", nullable = false, precision = 12, scale = 2)
    private BigDecimal valor;
    @Column(name = "status", nullable = false, length = 15)
    private String status;
    @Column(name = "data_liberacao")
    private LocalDateTime dataLiberacao;
}

