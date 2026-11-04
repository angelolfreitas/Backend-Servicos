package com.elos.negociacao_service.repository;

import com.elos.negociacao_service.negociacao.Proposta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropostaRepository extends JpaRepository<Proposta, Long> {}