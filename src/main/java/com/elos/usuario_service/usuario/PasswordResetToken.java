package com.elos.usuario_service.usuario;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

///
/// Recurso para reset de senha. Será uma persistência de acesso de um usuário
/// para que sua senha possa ser alterada após uma requisição enviada por email.
/// Só pode ser usada uma vez.
@Entity
@Table(name = "password_reset_token")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 64)
    private String tokenHash;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private Instant expiresAt;

    private boolean used;
}