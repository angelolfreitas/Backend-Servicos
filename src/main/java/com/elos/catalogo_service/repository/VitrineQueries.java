package com.elos.catalogo_service.repository;

final class VitrineQueries {
    static final String BASE = """
        select new com.elos.catalogo_service.dto.catalogo.VitrineItem(
            p.idPrestador, p.fotoPerfil, p.cidade, p.regiao, p.descricao,
            r.media, coalesce(r.total, 0L),
            (select min(s.valor) from Servico s where s.idPrestador = p.idPrestador and s.status = 'ATIVO'),
            (select count(s) from Servico s where s.idPrestador = p.idPrestador and s.status = 'ATIVO'))
        from Portfolio p
        join PrestadorHabilitado h on h.idPrestador = p.idPrestador
        left join ReputacaoPrestador r on r.idUsuario = h.idUsuario
        where exists (select 1 from Servico sa where sa.idPrestador = p.idPrestador and sa.status = 'ATIVO')
          and (:notaMinima = 0 or coalesce(r.media, 0) >= :notaMinima)
          and (:idCategoria = 0 or exists (
                select 1 from Servico sc, TipoServico tc
                where sc.idPrestador = p.idPrestador and sc.status = 'ATIVO'
                  and tc.idTipoServico = sc.idTipoServico and tc.idCategoria = :idCategoria))
          and (:semTexto = true
               or locate(:texto, lower(p.descricao)) > 0
               or locate(:texto, lower(p.cidade)) > 0
               or exists (
                    select 1 from Servico st, TipoServico tt
                    where st.idPrestador = p.idPrestador and st.status = 'ATIVO'
                      and tt.idTipoServico = st.idTipoServico
                      and (locate(:texto, lower(st.descricao)) > 0 or locate(:texto, lower(tt.nome)) > 0)))
        """;
}