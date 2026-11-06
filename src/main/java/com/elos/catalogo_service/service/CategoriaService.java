package com.elos.catalogo_service.service;

import com.elos.catalogo_service.catalogo.Categoria;
import com.elos.catalogo_service.catalogo.TipoServico;
import com.elos.catalogo_service.dto.catalogo.CategoriaRequest;
import com.elos.catalogo_service.dto.catalogo.CategoriaResponse;
import com.elos.catalogo_service.dto.catalogo.TipoServicoRequest;
import com.elos.catalogo_service.dto.catalogo.TipoServicoResponse;
import com.elos.catalogo_service.repository.CategoriaRepository;
import com.elos.catalogo_service.repository.TipoServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {
    private final CategoriaRepository categorias;
    private final TipoServicoRepository tipos;

    public List<CategoriaResponse> listarAtivas() {
        return categorias.findByStatusAtivo("ATIVO").stream().map(CategoriaResponse::criar).toList();
    }

    public List<CategoriaResponse> listarTodas() {
        return categorias.findAll().stream().map(CategoriaResponse::criar).toList();
    }

    public List<TipoServicoResponse> listarTiposAtivos(Long idCategoria) {
        return tipos.findByIdCategoriaAndStatusAtivo(idCategoria, "ATIVO")
                .stream().map(TipoServicoResponse::criar).toList();
    }

    public CategoriaResponse criar(CategoriaRequest req) {
        if (categorias.existsByNomeIgnoreCase(req.nome()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Categoria já existe");
        Categoria c = new Categoria();
        c.setNome(req.nome());
        c.setStatusAtivo("ATIVO");
        return CategoriaResponse.criar(categorias.save(c));
    }

    public CategoriaResponse renomear(Long id, CategoriaRequest req) {
        Categoria c = buscar(id);
        c.setNome(req.nome());
        return CategoriaResponse.criar(categorias.save(c));
    }

    public CategoriaResponse alterarStatus(Long id, boolean ativo) {
        Categoria c = buscar(id);
        c.setStatusAtivo(ativo ? "ATIVO" : "INATIVO");
        return CategoriaResponse.criar(categorias.save(c));
    }

    public TipoServicoResponse criarTipo(Long idCategoria, TipoServicoRequest req) {
        buscar(idCategoria);
        if (tipos.existsByIdCategoriaAndNomeIgnoreCase(idCategoria, req.nome()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Tipo já existe nesta categoria");
        TipoServico t = new TipoServico();
        t.setIdCategoria(idCategoria);
        t.setNome(req.nome());
        t.setStatusAtivo("ATIVO");
        return TipoServicoResponse.criar(tipos.save(t));
    }

    public TipoServicoResponse alterarStatusTipo(Long idTipo, boolean ativo) {
        TipoServico t = tipos.findById(idTipo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tipo não encontrado"));
        t.setStatusAtivo(ativo ? "ATIVO" : "INATIVO");
        return TipoServicoResponse.criar(tipos.save(t));
    }

    private Categoria buscar(Long id) {
        return categorias.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada"));
    }
}