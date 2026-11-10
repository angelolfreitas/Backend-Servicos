package com.elos.usuario_service.dto;

import com.elos.usuario_service.usuario.Documento;
import com.elos.usuario_service.usuario.estado.StatusAnalise;
import com.elos.usuario_service.usuario.estado.TipoDocumento;

import java.time.LocalDate;

public record DocumentoResponse(Long id, Long idPrestador, TipoDocumento tipoDocumento,
                                LocalDate dataEnvio, StatusAnalise statusAnalise) {
    public static DocumentoResponse criar(Documento d) {
        return new DocumentoResponse(d.getIdDocumento(), d.getIdPrestador(), d.getTipoDocumento(),
                d.getDataEnvio(), d.getStatusAnalise());
    }
}
