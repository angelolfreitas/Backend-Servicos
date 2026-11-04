package com.elos.catalogo_service.repository;

import com.elos.catalogo_service.catalogo.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    List<Categoria> findByStatusAtivo(String statusAtivo);
    boolean existsByNomeIgnoreCase(String nome);
}
