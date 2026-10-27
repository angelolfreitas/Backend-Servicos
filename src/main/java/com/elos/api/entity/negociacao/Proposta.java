package com.elos.api.entity.negociacao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/** Entidade do modelo ELOS. Relacionamentos mantidos como IDs nesta primeira etapa para facilitar futura divisão em microsserviços. */
@Setter
@Getter
@Entity
@Table(name = "proposta")
public class Proposta implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proposta")
    private Long idProposta;

    @Column(name = "id_cliente")
    private Long idCliente;

    @Column(name = "id_prestador")
    private Long idPrestador;

    @Column(name = "id_servico")
    private Long idServico;

    @Column(name = "valor")
    private BigDecimal valor;

    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @Column(name = "validade")
    private LocalDate validade;

    @Column(name = "status")
    private String status;

    @Column(name = "descricao")
    private String descricao;
}
