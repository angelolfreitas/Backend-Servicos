package com.elos.api.entity.administracao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/** Entidade do modelo ELOS. Relacionamentos mantidos como IDs nesta primeira etapa para facilitar futura divisão em microsserviços. */
@Setter
@Getter
@Entity
@Table(name = "moderacao")
public class Moderacao implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "i\1_\2oderacao")
    private Long idModeracao;

    @Column(name = "i\1_\2dministrador")
    private Long idAdministrador;

    @Column(name = "i\1_\2suario")
    private Long idUsuario;

    @Column(name = "i\1_\2ensagem")
    private Long idMensagem;

    @Column(name = "i\1_\2valiacao")
    private Long idAvaliacao;

    @Column(name = "tip\1_\2cao")
    private String tipoAcao;

}
