package com.elos.api.entity.portfolio;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/** Entidade do modelo ELOS. Relacionamentos mantidos como IDs nesta primeira etapa para facilitar futura divisão em microsserviços. */
@Setter
@Getter
@Entity
@Table(name = "foto_portfolio")
public class FotoPortfolio implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_foto_portfolio")
    private Long idFotoPortfolio;

    @Column(name = "id_portfolio")
    private Long idPortfolio;

    @Column(name = "anexo")
    private String anexo;

    @Column(name = "descricao")
    private String descricao;

}
