package com.elos.api.entity.financeiro;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/** Entidade do modelo ELOS. Relacionamentos mantidos como IDs nesta primeira etapa para facilitar futura divisão em microsserviços. */
@Setter
@Getter
@Entity
@Table(name = "comissao_pagamento")
public class ComissaoPagamento implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comissao_pagamento")
    private Long idComissaoPagamento;

    @Column(name = "id_pagamento")
    private Long idPagamento;

    @Column(name = "percentual")
    private BigDecimal percentual;

    @Column(name = "valor")
    private BigDecimal valor;


}
