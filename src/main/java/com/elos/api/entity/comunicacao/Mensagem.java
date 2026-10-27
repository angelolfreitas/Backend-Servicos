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
    @Column(name = "i\1_\2ensagem")
    private Long idMensagem;

    @Column(name = "i\1_\2ontratacao")
    private Long idContratacao;

    @Column(name = "mensagem")
    private String mensagem;

    @Column(name = "dat\1_\2ora")
    private LocalDateTime dataHora;

    @Column(name = "i\1_\2suari\1_\2emetente")
    private Long idUsuarioRemetente;

}
