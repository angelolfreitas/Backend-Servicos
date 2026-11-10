package com.elos.usuario_service.dto;

import com.elos.usuario_service.usuario.Prestador;
import com.elos.usuario_service.usuario.estado.StatusAnalise;
import com.elos.usuario_service.usuario.estado.TipoPrestador;

public record PrestadorResponse(Long idPrestador, Long idUsuario, TipoPrestador tipoPrestador,
                                String cpf, String cnpj, StatusAnalise statusAprovacao) {
    public static PrestadorResponse criar(Prestador p) {
        return new PrestadorResponse(p.getIdPrestador(), p.getIdUsuario(), p.getTipoPrestador(),
                p.getCpf(), p.getCnpj(), p.getStatusAprovacao());
    }
}