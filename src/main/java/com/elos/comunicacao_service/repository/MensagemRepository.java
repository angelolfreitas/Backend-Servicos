package com.elos.comunicacao_service.repository;

import com.elos.comunicacao_service.chat.Mensagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensagemRepository extends JpaRepository<Mensagem, Long> {
    List<Mensagem> findByIdContratacaoOrderByDataHoraAscIdMensagemAsc(Long idContratacao);
}
