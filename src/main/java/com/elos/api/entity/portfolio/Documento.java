package com.elos.api.entity.portfolio;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/** Entidade do modelo ELOS. Relacionamentos mantidos como IDs nesta primeira etapa para facilitar futura divisão em microsserviços. */
@Setter
@Getter
@Entity
@Table(name = "documento")
public class Documento implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "i\1_\2ocumento")
    private Long idDocumento;

    @Column(name = "i\1_\2restador")
    private Long idPrestador;

    @Column(name = "tip\1_\2ocumento")
    private String tipoDocumento;

    @Column(name = "anexo")
    private String anexo;

    @Column(name = "dat\1_\2nvio")
    private LocalDate dataEnvio;

    @Column(name = "statu\1_\2nalise")
    private String statusAnalise;

}
