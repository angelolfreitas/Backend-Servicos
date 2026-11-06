package com.elos.catalogo_service.controller;

import com.elos.catalogo_service.service.CategoriaService;
import com.elos.catalogo_service.dto.catalogo.CategoriaRequest;
import com.elos.catalogo_service.dto.catalogo.CategoriaResponse;
import com.elos.catalogo_service.dto.catalogo.TipoServicoRequest;
import com.elos.catalogo_service.dto.catalogo.TipoServicoResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/categorias")
@PreAuthorize("hasAuthority('admin::manage')")
@RequiredArgsConstructor
public class CategoriaAdminController {
    private final CategoriaService service;

    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }


    @PatchMapping("/{id}/status")
    public ResponseEntity<CategoriaResponse> status(@PathVariable Long id, @RequestParam boolean ativo) {
        return ResponseEntity.ok(service.alterarStatus(id, ativo));
    }

    @PatchMapping("/tipos/{idTipo}/status")
    public ResponseEntity<TipoServicoResponse> statusTipo(@PathVariable Long idTipo, @RequestParam boolean ativo) {
        return ResponseEntity.ok(service.alterarStatusTipo(idTipo, ativo));
    }

    @PostMapping
    public ResponseEntity<CategoriaResponse> criar(@Valid @RequestBody CategoriaRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> renomear(@PathVariable Long id, @Valid @RequestBody CategoriaRequest req) {
        return ResponseEntity.ok(service.renomear(id, req));
    }

    @PostMapping("/{id}/tipos")
    public ResponseEntity<TipoServicoResponse> criarTipo(@PathVariable Long id, @Valid @RequestBody TipoServicoRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criarTipo(id, req));
    }
}