package com.elos.usuario_service.usuario;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * Entidade Administrador. Herdada na tabela de usuários para evitar a redundância de campos.
 * Implementa serializable para transmissao de dados entre os microsserviços.
 * Utiliza os recursos do lombok para economizar código boilerplate
 */
@Setter
@Getter
@Entity
@Table(name = "administrador")
public class Administrador implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_administrador")
    private Long idAdministrador;

    @Column(name = "id_usuario")
    private Long idUsuario;


}
