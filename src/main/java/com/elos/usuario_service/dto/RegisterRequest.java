package com.elos.usuario_service.dto;


import com.elos.usuario_service.usuario.role.Role;

public record RegisterRequest(String username, String email, String password, String telefone, String tipoConta, String statusConta, Role role) {
    public static RegisterRequest noRole(String username, String email, String password, String telefone, String tipoConta, String statusConta){
        return new RegisterRequest(username, email, password, telefone, tipoConta, statusConta, Role.USER);
    }
}
