package com.elos.usuario_service.usuario;

import com.elos.usuario_service.usuario.estado.StatusAnalise;
import com.elos.usuario_service.usuario.estado.TipoDocumento;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 *  Entidade documento. Possui associado um único prestador.
 *  Como campos descritivos, possui o tipo do documento, os anexos, a data de envio
 *  e o estado de análise.
 *  */
@Setter
@Getter
@Entity
@Table(name = "documento")
public class Documento implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_documento")
    private Long idDocumento;

    @Column(name = "id_prestador")
    private Long idPrestador;

    @Column(name = "anexo")
    private String anexo;

    @Column(name = "data_envio")
    private LocalDate dataEnvio;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento")
    private TipoDocumento tipoDocumento;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_analise")
    private StatusAnalise statusAnalise;

}
