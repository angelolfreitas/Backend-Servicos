package com.elos.usuario_service.dto;

import com.elos.usuario_service.usuario.Documento;

import java.time.LocalDate;

public record DocumentoResponse(Long id, Long idPrestador, String tipoDocumento,
                                LocalDate dataEnvio, String statusAnalise) {
    public static DocumentoResponse criar(Documento d) {
        return new DocumentoResponse(d.getIdDocumento(), d.getIdPrestador(), d.getTipoDocumento(),
                d.getDataEnvio(), d.getStatusAnalise());
    }
}
