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
    @Column(name = "id_consentimento")
    private Long idConsentimento;

    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "tipo_consentimento")
    private String tipoConsentimento;

    @Column(name = "data_hora")
    private LocalDateTime dataHora;


}
