package com.elos.catalogo_service.repository;

import com.elos.catalogo_service.catalogo.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {
    List<Servico> findByIdPrestador(Long idPrestador);
}
