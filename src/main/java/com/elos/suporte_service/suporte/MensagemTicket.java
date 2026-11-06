package com.elos.suporte_service.suporte;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "mensagem_ticket")
@Getter
@Setter
@NoArgsConstructor
public class MensagemTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mensagem_ticket")
    private Long idMensagemTicket;

    @Column(name = "id_ticket", nullable = false)
    private Long idTicket;

    @Column(name = "id_usuario_remetente", nullable = false)
    private Long idUsuarioRemetente;

    @Column(name = "mensagem", nullable = false, length = 2000)
    private String mensagem;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;
}