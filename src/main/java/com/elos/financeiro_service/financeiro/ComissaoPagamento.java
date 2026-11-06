package com.elos.financeiro_service.financeiro;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "comissao_pagamento")
@Getter
@Setter
@NoArgsConstructor
public class ComissaoPagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comissao_pagamento")
    private Long idComissaoPagamento;
    @Column(name = "id_pagamento", nullable = false)
    private Long idPagamento;
    @Column(name = "percentual", nullable = false, precision = 5, scale = 2)
    private BigDecimal percentual;
    @Column(name = "valor", nullable = false, precision = 12, scale = 2)
    private BigDecimal valor;
}