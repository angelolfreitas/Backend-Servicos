package com.elos.suporte_service.suporte;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "moderacao")
@Getter
@Setter
@NoArgsConstructor
public class Moderacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_moderacao")
    private Long idModeracao;

    @Column(name = "id_administrador", nullable = false)
    private Long idAdministrador;

    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "id_avaliacao")
    private Long idAvaliacao;

    @Column(name = "tipo_acao", nullable = false, length = 30)
    private String tipoAcao;

    @Column(name = "motivo", nullable = false, length = 500)
    private String motivo;

    @Column(name = "id_ticket")
    private Long idTicket;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;
}