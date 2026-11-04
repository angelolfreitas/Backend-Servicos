package com.elos.catalogo_service;

import com.elos.catalogo_service.dto.catalogo.CategoriaResponse;
import com.elos.catalogo_service.dto.catalogo.TipoServicoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
public class CategoriaController {
    private final CategoriaService service;

    @GetMapping
    @PreAuthorize("hasAuthority('user::read')")
    public ResponseEntity<List<CategoriaResponse>> listar() {
        return ResponseEntity.ok(service.listarAtivas());
    }

    @GetMapping("/{id}/tipos")
    @PreAuthorize("hasAuthority('user::read')")
    public ResponseEntity<List<TipoServicoResponse>> tipos(@PathVariable Long id) {
        return ResponseEntity.ok(service.listarTiposAtivos(id));
    }
}