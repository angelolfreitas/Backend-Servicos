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
@Table(name = "mensagem_ticket")
public class MensagemTicket implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "i\1_\2ensage\1_\2icket")
    private Long idMensagemTicket;

    @Column(name = "i\1_\2icket")
    private Long idTicket;

    @Column(name = "mensagem")
    private String mensagem;

    @Column(name = "dat\1_\2ora")
    private LocalDateTime dataHora;


}
