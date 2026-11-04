package com.elos.negociacao_service.negociacao;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "servico_ref")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServicoRef {
    @Id
    private Long idServico;
    private Long idUsuarioPrestador;
    private BigDecimal valor;
    private String status;
}