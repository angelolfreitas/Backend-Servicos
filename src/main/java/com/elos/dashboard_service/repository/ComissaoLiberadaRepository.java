package com.elos.dashboard_service.repository;

import com.elos.dashboard_service.dashboard.ComissaoLiberada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ComissaoLiberadaRepository extends JpaRepository<ComissaoLiberada, Long> {

    @Query("select c.data as data, sum(c.valorComissao) as valor, count(c) as qtd "
            + "from ComissaoLiberada c where c.data >= :inicio group by c.data order by c.data")
    List<FaturamentoDia> porDia(@Param("inicio") LocalDate inicio);

    @Query("select coalesce(sum(c.valorComissao), 0) from ComissaoLiberada c where c.data = :data")
    BigDecimal somarDoDia(@Param("data") LocalDate data);

    @Query("select coalesce(sum(c.valorComissao), 0) from ComissaoLiberada c")
    BigDecimal somarTotal();
}
