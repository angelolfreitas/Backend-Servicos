package com.elos.usuario_service.usuario;

import com.elos.usuario_service.usuario.estado.StatusConta;
import com.elos.usuario_service.usuario.estado.TipoConta;
import com.elos.usuario_service.usuario.role.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;

/**
 * Entidade de usuários. Herdada por administradores, clientes e prestadores.
 * Possu campos de acesso essenciais ao sistema, comuns a todos (email, senha, nome).
 * Herda de userdetails para compatibilidade com o spring security.
 * Possui outros campos adicionais como telefone, tipo conta, status conta e data de cadastro.
 * possui o campo role. As roles delegam as ações que cada usuário tem o direito de fazer no sistema.
 * Podendo ser admin, manager ou user. Usuários sem roles só podem logar e criar contas. Para usar quaisquer
 * requsições nesta API você precisa estar autenticado com uma role.
 * O método está bloqueado foi um recurso encontrado para garantir que o usupario nao
 * apareça ou logue no sistema se for banido na moderação.
 * * */
@Setter
@Getter
@Entity
@Table(name = "usuario")
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Usuario implements Serializable, UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "nome")
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "senha")
    private String password;

    @Column(name = "telefone")
    private String telefone;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_conta")
    private TipoConta tipoConta;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_conta")
    private StatusConta statusConta;

    @Column(name = "data_cadastro")
    private LocalDate dataCadastro;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    public boolean estaBloqueado() {
        return statusConta != StatusConta.ATIVO;
    }


}
