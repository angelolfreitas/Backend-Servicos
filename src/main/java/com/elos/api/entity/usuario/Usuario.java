package com.elos.api.entity.usuario;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/** Entidade do modelo ELOS. Relacionamentos mantidos como IDs nesta primeira etapa para facilitar futura divisão em microsserviços. */
@Setter
@Getter
@Entity
@Table(name = "usuario")
@NoArgsConstructor
public class Usuario implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "i\1_\2suario")
    private Long idUsuario;

    @Column(name = "nome")
    private String nome;

    @Column(name = "email")
    private String email;

    @Column(name = "senha")
    private String senha;

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "tip\1_\2onta")
    private String tipoConta;

    @Column(name = "statu\1_\2onta")
    private String statusConta;

    @Column(name = "dat\1_\2adastro")
    private LocalDate dataCadastro;
}
