package com.elos.suporte_service.repository;

import com.elos.suporte_service.suporte.Moderacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModeracaoRepository extends JpaRepository<Moderacao, Long> {
    List<Moderacao> findTop50ByOrderByIdModeracaoDesc();
}