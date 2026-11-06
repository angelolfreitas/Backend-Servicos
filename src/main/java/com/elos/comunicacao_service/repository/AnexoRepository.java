package com.elos.comunicacao_service.repository;

import com.elos.comunicacao_service.chat.Anexo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface AnexoRepository extends JpaRepository<Anexo, Long> {
    List<Anexo> findByIdMensagemIn(Collection<Long> ids);
}
