package com.elos.api.entity.usuario.role;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Authorities {
    ADMIN_READ("admin::read"),
    ADMIN_WRITE("admin::write"),
    ADMIN_DELETE("admin::delete"),
    ADMIN_MANAGE("admin::manage"),
    MANAGER_READ("manager::read"),
    MANAGER_WRITE("manager::write"),
    MANAGER_DELETE("manager::delete"),
    MANAGER_MANAGE("manager::manage"),
    USER_READ("user::read"),
    USER_WRITE("user::write"),
    USER_DELETE("user::delete");
    @Getter
    private final String name;
}