package com.elos.api.entity.comunicacao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/** Entidade do modelo ELOS. Relacionamentos mantidos como IDs nesta primeira etapa para facilitar futura divisão em microsserviços. */
@Setter
@Getter
@Entity
@Table(name = "mensagem")
public class Mensagem implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mensagem")
    private Long idMensagem;

    @Column(name = "id_contratacao")
    private Long idContratacao;

    @Column(name = "mensagem")
    private String mensagem;

    @Column(name = "data_hora")
    private LocalDateTime dataHora;

    @Column(name = "id_usuario_remetente")
    private Long idUsuarioRemetente;

}
