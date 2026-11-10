package com.elos.usuario_service.usuario.role;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
/// As roles são funções que todo usuário tem ao se autenticar nesta API.
/// Em ordem de hierarquia: user->manager->admin.
/// A rooem admin possui todas as roles anteriores.
/// AS funções são divididas em authorities, que descrevem ações associadas a cada role e são usadas
/// pelo method security em todos os controllers para restringir o acesso das entidades aos métodos http.
@RequiredArgsConstructor
public enum Role {
    ADMIN
            (
                    Set.of(
                            Authorities.ADMIN_MANAGE,
                            Authorities.ADMIN_WRITE,
                            Authorities.ADMIN_DELETE,
                            Authorities.ADMIN_READ,
                            Authorities.MANAGER_MANAGE,
                            Authorities.MANAGER_WRITE,
                            Authorities.MANAGER_DELETE,
                            Authorities.MANAGER_READ,
                            Authorities.USER_WRITE,
                            Authorities.USER_DELETE,
                            Authorities.USER_READ
                    )
            ),
    MANAGER
            (
                    Set.of(
                            Authorities.MANAGER_MANAGE,
                            Authorities.MANAGER_WRITE,
                            Authorities.MANAGER_DELETE,
                            Authorities.MANAGER_READ,
                            Authorities.USER_WRITE,
                            Authorities.USER_DELETE,
                            Authorities.USER_READ
                    )
            ),
    USER
            (
                    Set.of(
                            Authorities.USER_READ,
                            Authorities.USER_WRITE,
                            Authorities.USER_DELETE
                    )
            )
    ;
    @Getter
    private final Set<Authorities> roleAuthorities;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = new java.util.ArrayList<>(getRoleAuthorities()
                .stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                .toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
