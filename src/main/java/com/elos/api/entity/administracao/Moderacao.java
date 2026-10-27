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
    @Column(name = "id_moderacao")
    private Long idModeracao;

    @Column(name = "id_administrador")
    private Long idAdministrador;

    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "id_mensagem")
    private Long idMensagem;

    @Column(name = "id_avaliacao")
    private Long idAvaliacao;

    @Column(name = "tipo_acao")
    private String tipoAcao;

}
