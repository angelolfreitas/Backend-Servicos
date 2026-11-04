package com.elos.usuario_service.dto;

import com.elos.usuario_service.usuario.Prestador;

public record PrestadorResponse(Long idPrestador, Long idUsuario, String tipoPrestador,
                                String cpf, String cnpj, String statusAprovacao) {
    public static PrestadorResponse criar(Prestador p) {
        return new PrestadorResponse(p.getIdPrestador(), p.getIdUsuario(), p.getTipoPrestador(),
                p.getCpf(), p.getCnpj(), p.getStatusAprovacao());
    }
}