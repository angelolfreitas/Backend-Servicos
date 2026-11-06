package com.elos.suporte_service.suporte;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "ticket")
@Getter
@Setter
@NoArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ticket")
    private Long idTicket;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "id_administrador")
    private Long idAdministrador;

    @Column(name = "protocolo", unique = true, length = 20)
    private String protocolo;

    @Column(name = "categoria_problema", nullable = false, length = 30)
    private String categoriaProblema;

    @Column(name = "id_contratacao")
    private Long idContratacao;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "data_abertura", nullable = false)
    private LocalDateTime dataAbertura;

    @Column(name = "data_fechamento")
    private LocalDateTime dataFechamento;
}