package com.elos.reputacao_service.reputacao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "avaliacao",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_contratacao", "id_usuario_avaliador"}))
@Getter
@Setter
@NoArgsConstructor
public class Avaliacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_avaliacao")
    private Long idAvaliacao;

    @Column(name = "id_contratacao", nullable = false)
    private Long idContratacao;

    @Column(name = "id_usuario_avaliador", nullable = false)
    private Long idUsuarioAvaliador;

    @Column(name = "id_usuario_avaliado", nullable = false)
    private Long idUsuarioAvaliado;

    @Column(name = "papel_avaliado", nullable = false, length = 10)
    private String papelAvaliado;

    @Column(name = "nota", nullable = false)
    private Integer nota;

    @Column(name = "comentario", length = 500)
    private String comentario;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Column(name = "oculta", nullable = false)
    private boolean oculta;

    @Column(name = "id_admin_moderador")
    private Long idAdminModerador;

    @Column(name = "data_moderacao")
    private LocalDateTime dataModeracao;
}