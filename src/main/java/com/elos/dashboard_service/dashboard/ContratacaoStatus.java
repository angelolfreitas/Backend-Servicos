package com.elos.dashboard_service.dashboard;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "contratacao_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContratacaoStatus {
    @Id
    private Long idContratacao;
    private String status;
}