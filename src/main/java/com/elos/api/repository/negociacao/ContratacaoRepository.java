package com.elos.api.repository.negociacao;

import com.elos.api.entity.negociacao.Contratacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContratacaoRepository extends JpaRepository<Contratacao, Long> {
}
