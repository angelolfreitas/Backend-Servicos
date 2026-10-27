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
    @Column(name = "id_avaliacao")
    private Long idAvaliacao;

    @Column(name = "id_usuario_avaliador")
    private Long idUsuarioAvaliador;

    @Column(name = "id_contratacao")
    private Long idContratacao;

    @Column(name = "nota")
    private Integer nota;

    @Column(name = "comentario")
    private String comentario;

    @Column(name = "id_usuario_avaliado")
    private Long idUsuarioAvaliado;


}
