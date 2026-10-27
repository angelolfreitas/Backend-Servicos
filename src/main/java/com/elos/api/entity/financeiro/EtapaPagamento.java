package com.elos.api.entity.financeiro;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Entidade do modelo ELOS. Relacionamentos mantidos como IDs nesta primeira etapa para facilitar futura divisão em microsserviços. */
@Setter
@Getter
@Entity
@Table(name = "etapa_pagamento")
public class EtapaPagamento implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_etapa_pagamento")
    private Long idEtapaPagamento;

    @Column(name = "id_pagamento")
    private Long idPagamento;

    @Column(name = "percentual")
    private BigDecimal percentual;

    @Column(name = "valor")
    private BigDecimal valor;

    @Column(name = "status")
    private String status;

    @Column(name = "data_liberacao")
    private LocalDateTime dataLiberacao;
}
