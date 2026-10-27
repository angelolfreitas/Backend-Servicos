package com.elos.api.repository.privacidade;

import com.elos.api.entity.privacidade.Consentimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsentimentoRepository extends JpaRepository<Consentimento, Long> {
}
