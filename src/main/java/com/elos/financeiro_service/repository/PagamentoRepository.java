package com.elos.financeiro_service.repository;

import com.elos.financeiro_service.financeiro.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Optional;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
    Optional<Pagamento> findByIdContratacao(Long idContratacao);

    @Query("select coalesce(sum(p.valorLiquido), 0) from Pagamento p "
            + "where p.idPrestador = :id and p.status in :status")
    BigDecimal somarLiquido(@Param("id") Long idPrestador, @Param("status") Collection<String> status);
}