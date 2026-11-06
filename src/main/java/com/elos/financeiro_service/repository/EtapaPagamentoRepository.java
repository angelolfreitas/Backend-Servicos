package com.elos.financeiro_service.repository;

import com.elos.financeiro_service.financeiro.EtapaPagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EtapaPagamentoRepository extends JpaRepository<EtapaPagamento, Long> {
    List<EtapaPagamento> findByIdPagamentoOrderByNumero(Long idPagamento);
}