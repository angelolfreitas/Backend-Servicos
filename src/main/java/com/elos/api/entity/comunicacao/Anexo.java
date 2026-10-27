package com.elos.api.entity.comunicacao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/** Entidade do modelo ELOS. Relacionamentos mantidos como IDs nesta primeira etapa para facilitar futura divisão em microsserviços. */
@Setter
@Getter
@Entity
@Table(name = "anexo")
public class Anexo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "i\1_\2nexo")
    private Long idAnexo;

    @Column(name = "i\1_\2ensagem")
    private Long idMensagem;

    @Column(name = "arquivo")
    private String arquivo;

    @Column(name = "tip\1_\2rquivo")
    private String tipoArquivo;

}
