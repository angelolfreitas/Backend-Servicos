package com.elos.usuario_service.dto;


import com.elos.usuario_service.usuario.estado.TipoConta;
import com.elos.usuario_service.usuario.role.Role;
import jakarta.validation.constraints.*;

public record RegisterRequest(
        @NotBlank(message = "Nome obrigatório")
        @Size(max = 80, message = "Nome com no máximo 80 caracteres")
        String username,

        @NotBlank(message = "E-mail obrigatório")
        @Email(message = "E-mail inválido")
        @Size(max = 120, message = "E-mail com no máximo 120 caracteres")
        String email,

        // 72 é o limite do BCrypt
        @NotBlank(message = "Senha obrigatória")
        @Size(min = 8, max = 72, message = "Senha deve ter de 8 a 72 caracteres")
        String password,

        @NotBlank(message = "Telefone obrigatório")
        @Pattern(regexp = "\\d{10,11}", message = "Telefone deve ter 10 ou 11 dígitos")
        String telefone,

        @NotNull(message = "Tipo de conta obrigatório (cliente ou prestador")
        TipoConta tipoConta) {}
