package com.elos.financeiro_service.financeiro;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "contratacao_ref")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContratacaoRef {
    @Id
    private Long idContratacao;
    private Long idCliente;
    private Long idPrestador;
    @Column(precision = 12, scale = 2)
    private BigDecimal valor;
    private String status;
}