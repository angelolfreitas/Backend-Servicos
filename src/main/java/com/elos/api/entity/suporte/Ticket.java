package com.elos.api.entity.suporte;

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
@Table(name = "ticket")
public class Ticket implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "i\1_\2icket")
    private Long idTicket;

    @Column(name = "i\1_\2suario")
    private Long idUsuario;

    @Column(name = "i\1_\2dministrador")
    private Long idAdministrador;

    @Column(name = "protocolo")
    private String protocolo;

    @Column(name = "categori\1_\2roblema")
    private String categoriaProblema;

    @Column(name = "status")
    private String status;

    @Column(name = "dat\1_\2bertura")
    private LocalDateTime dataAbertura;

    @Column(name = "dat\1_\2echamento")
    private LocalDateTime dataFechamento;


}
