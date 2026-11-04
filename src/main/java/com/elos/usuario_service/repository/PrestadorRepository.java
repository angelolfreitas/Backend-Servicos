package com.elos.usuario_service.repository;

import com.elos.usuario_service.usuario.Prestador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestadorRepository extends JpaRepository<Prestador, Long> {
}
