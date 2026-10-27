package com.elos.api.repository.administracao;

import com.elos.api.entity.administracao.Moderacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModeracaoRepository extends JpaRepository<Moderacao, Long> {
}
