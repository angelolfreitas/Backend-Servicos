package com.elos.usuario_service.dto;


import com.elos.usuario_service.usuario.role.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

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

        @NotBlank(message = "Tipo de conta obrigatório")
        @Pattern(regexp = "CLIENTE|PRESTADOR", message = "tipoConta deve ser CLIENTE ou PRESTADOR")
        String tipoConta) {}
