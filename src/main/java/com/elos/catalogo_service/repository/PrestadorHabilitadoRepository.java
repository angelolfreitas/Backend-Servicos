package com.elos.catalogo_service.repository;

import com.elos.catalogo_service.catalogo.PrestadorHabilitado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PrestadorHabilitadoRepository extends JpaRepository<PrestadorHabilitado, Long> {
    Optional<PrestadorHabilitado> findByIdUsuario(Long idUsuario);
}