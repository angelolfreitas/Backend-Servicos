package com.elos.api.repository.financeiro;

import com.elos.api.entity.financeiro.EtapaPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EtapaPagamentoRepository extends JpaRepository<EtapaPagamento, Long> {
}
