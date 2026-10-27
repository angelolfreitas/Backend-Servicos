package com.elos.api.entity.catalogo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/** Entidade do modelo ELOS. Relacionamentos mantidos como IDs nesta primeira etapa para facilitar futura divisão em microsserviços. */
@Setter
@Getter
@Entity
@Table(name = "tipo_servico")
public class TipoServico implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_servico")
    private Long idTipoServico;

    @Column(name = "id_categoria")
    private Long idCategoria;

    @Column(name = "nome")
    private String nome;

    @Column(name = "status_ativo")
    private String statusAtivo;

}
