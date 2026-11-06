package com.elos.dashboard_service.dashboard;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "comissao_liberada")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComissaoLiberada {
    @Id
    private Long idContratacao;
    @Column(nullable = false)
    private LocalDate data;
    @Column(name = "valor_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorTotal;
    @Column(name = "valor_comissao", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorComissao;
}