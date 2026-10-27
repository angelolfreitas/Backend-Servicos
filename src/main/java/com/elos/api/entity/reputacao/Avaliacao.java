package com.elos.api.entity.reputacao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/** Entidade do modelo ELOS. Relacionamentos mantidos como IDs nesta primeira etapa para facilitar futura divisão em microsserviços. */
@Setter
@Getter
@Entity
@Table(name = "avaliacao")
public class Avaliacao implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "i\1_\2valiacao")
    private Long idAvaliacao;

    @Column(name = "i\1_\2suari\1_\2valiador")
    private Long idUsuarioAvaliador;

    @Column(name = "i\1_\2ontratacao")
    private Long idContratacao;

    @Column(name = "nota")
    private Integer nota;

    @Column(name = "comentario")
    private String comentario;

    @Column(name = "i\1_\2suari\1_\2valiado")
    private Long idUsuarioAvaliado;


}
