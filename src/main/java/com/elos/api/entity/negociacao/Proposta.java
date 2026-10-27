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
    @Column(name = "i\1_\2roposta")
    private Long idProposta;

    @Column(name = "i\1_\2liente")
    private Long idCliente;

    @Column(name = "i\1_\2restador")
    private Long idPrestador;

    @Column(name = "i\1_\2ervico")
    private Long idServico;

    @Column(name = "valor")
    private BigDecimal valor;

    @Column(name = "dat\1_\2nicio")
    private LocalDate dataInicio;

    @Column(name = "validade")
    private LocalDate validade;

    @Column(name = "status")
    private String status;

    @Column(name = "descricao")
    private String descricao;
}
