package com.elos.reputacao_service.repository;

import com.elos.reputacao_service.dto.ResumoNota;
import com.elos.reputacao_service.reputacao.Avaliacao;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {
    boolean existsByIdContratacaoAndIdUsuarioAvaliador(Long idContratacao, Long idUsuarioAvaliador);

    List<Avaliacao> findByIdContratacaoAndOcultaFalse(Long idContratacao);

    List<Avaliacao> findByIdUsuarioAvaliadoAndPapelAvaliadoAndOcultaFalseOrderByIdAvaliacaoDesc(
            Long idUsuarioAvaliado, String papelAvaliado, Pageable pageable);

    List<Avaliacao> findTop50ByOrderByIdAvaliacaoDesc();

    @Query("""
           select new com.elos.reputacao_service.dto.ResumoNota(avg(a.nota), count(a))
           from Avaliacao a
           where a.idUsuarioAvaliado = :id and a.papelAvaliado = :papel and a.oculta = false
           """)
    ResumoNota resumo(@Param("id") Long id, @Param("papel") String papel);
}