package com.elos.api.entity.negociacao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/** Entidade do modelo ELOS. Relacionamentos mantidos como IDs nesta primeira etapa para facilitar futura divisão em microsserviços. */
@Setter
@Getter
@Entity
@Table(name = "contratacao")
public class Contratacao implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contratacao")
    private Long idContratacao;

    @Column(name = "id_cliente")
    private Long idCliente;

    @Column(name = "id_prestador")
    private Long idPrestador;

    @Column(name = "id_servico")
    private Long idServico;

    @Column(name = "id_proposta")
    private Long idProposta;

    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @Column(name = "data_finalizacao_prevista")
    private LocalDate dataFinalizacaoPrevista;

    @Column(name = "data_finalizacao")
    private LocalDate dataFinalizacao;

    @Column(name = "status")
    private String status;

}
