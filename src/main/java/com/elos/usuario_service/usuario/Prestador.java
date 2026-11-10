package com.elos.usuario_service.usuario;

import com.elos.usuario_service.usuario.estado.StatusAnalise;
import com.elos.usuario_service.usuario.estado.TipoPrestador;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * Tabela de prestador. Essa tabela herda de usuários.
 * Aqui temos campos adicionais como cpf, cnpj, tipo do prestador e status de aprovação.
 * Esse campod e status permite a visualizacao ou nao do prestador pelos clientes do sistema.
 *  */
@Setter
@Getter
@Entity
@Table(name = "prestador")
@NoArgsConstructor
public class Prestador implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prestador")
    private Long idPrestador;

    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "cnpj")
    private String cnpj;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_prestador")
    private TipoPrestador tipoPrestador;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_aprovacao")
    private StatusAnalise statusAprovacao;


}
