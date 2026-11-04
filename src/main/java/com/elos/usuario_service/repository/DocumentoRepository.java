package com.elos.usuario_service.repository;

import com.elos.usuario_service.usuario.Documento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentoRepository extends JpaRepository<Documento, Long> {
    List<Documento> findByIdPrestador(Long idPrestador);
}