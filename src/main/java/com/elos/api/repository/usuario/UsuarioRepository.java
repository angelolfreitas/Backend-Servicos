package com.elos.api.repository.usuario;

import com.elos.api.entity.usuario.Usuario;
import com.elos.api.entity.usuario.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String login);
    Optional<List<Usuario>> findByRole(Role role);
}
