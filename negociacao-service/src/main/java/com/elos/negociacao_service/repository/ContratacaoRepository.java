package com.elos.negociacao_service.repository;

import com.elos.negociacao_service.negociacao.Contratacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContratacaoRepository extends JpaRepository<Contratacao, Long> {
    List<Contratacao> findByIdClienteOrIdPrestadorOrderByIdContratacaoDesc(Long idCliente, Long idPrestador);
}
