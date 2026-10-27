package com.elos.api.entity.privacidade;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/** Entidade do modelo ELOS. Relacionamentos mantidos como IDs nesta primeira etapa para facilitar futura divisão em microsserviços. */
@Setter
@Getter
@Entity
@Table(name = "consentimento")
public class Consentimento implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "i\1_\2onsentimento")
    private Long idConsentimento;

    @Column(name = "i\1_\2suario")
    private Long idUsuario;

    @Column(name = "tip\1_\2onsentimento")
    private String tipoConsentimento;

    @Column(name = "dat\1_\2ora")
    private LocalDateTime dataHora;


}
