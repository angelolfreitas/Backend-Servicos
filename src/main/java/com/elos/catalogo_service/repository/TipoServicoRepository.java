package com.elos.catalogo_service.repository;

import com.elos.catalogo_service.catalogo.TipoServico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TipoServicoRepository extends JpaRepository<TipoServico, Long> {
    List<TipoServico> findByIdCategoriaAndStatusAtivo(Long idCategoria, String statusAtivo);
    boolean existsByIdCategoriaAndNomeIgnoreCase(Long idCategoria, String nome);
}