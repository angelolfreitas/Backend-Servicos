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
@Table(name = "portfolio")
public class Portfolio implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_portfolio")
    private Long idPortfolio;

    @Column(name = "id_prestador")
    private Long idPrestador;

    @Column(name = "foto_perfil")
    private String fotoPerfil;

    @Column(name = "curriculo")
    private String curriculo;

    @Column(name = "cidade")
    private String cidade;

    @Column(name = "regiao")
    private String regiao;

    @Column(name = "descricao")
    private String descricao;

}
