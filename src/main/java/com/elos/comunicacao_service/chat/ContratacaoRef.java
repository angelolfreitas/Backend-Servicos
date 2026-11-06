package com.elos.comunicacao_service.chat;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "contratacao_ref")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContratacaoRef {
    @Id
    private Long idContratacao;
    private Long idCliente;
    private Long idPrestador;
    private String status;
}