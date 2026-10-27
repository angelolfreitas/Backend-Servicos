package com.elos.api.repository.financeiro;

import com.elos.api.entity.financeiro.ComissaoPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComissaoPagamentoRepository extends JpaRepository<ComissaoPagamento, Long> {
}
