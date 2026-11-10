package com.elos.usuario_service.repository;

import com.elos.usuario_service.usuario.Prestador;
import com.elos.usuario_service.usuario.estado.StatusAnalise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrestadorRepository extends JpaRepository<Prestador, Long> {
    Optional<Prestador> findByIdUsuario(Long idUsuario);
    List<Prestador> findByStatusAprovacao(StatusAnalise statusAprovacao);
}
