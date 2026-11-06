package com.elos.financeiro_service.repository;

import com.elos.financeiro_service.financeiro.ComissaoPagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ComissaoPagamentoRepository extends JpaRepository<ComissaoPagamento, Long> {
    Optional<ComissaoPagamento> findByIdPagamento(Long idPagamento);
}