package com.elos.usuario_service.controller;

import com.elos.usuario_service.service.DocumentoService;
import com.elos.usuario_service.dto.DocumentoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('admin::manage')")
@RequiredArgsConstructor
public class DocumentoAdminController {
    private final DocumentoService service;

    @GetMapping("/prestadores/{id}/documentos")
    public ResponseEntity<List<DocumentoResponse>> listar(@PathVariable Long id) {
        return ResponseEntity.ok(service.doPrestador(id));
    }

    @GetMapping("/documentos/{id}/arquivo")
    public ResponseEntity<Resource> arquivo(@PathVariable Long id) {
        Resource r = service.arquivo(id);
        MediaType tipo = MediaTypeFactory.getMediaType(r).orElse(MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok().contentType(tipo).body(r);
    }

    @PatchMapping("/documentos/{id}/status")
    public ResponseEntity<DocumentoResponse> analisar(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(service.analisar(id, status));
    }
}
