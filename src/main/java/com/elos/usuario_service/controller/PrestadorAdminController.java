package com.elos.usuario_service.controller;

import com.elos.usuario_service.service.PrestadorService;
import com.elos.usuario_service.dto.PrestadorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
///Tela de prestadores para os admins.
/// Permite aprovar ou rejeitar uma candidatura e lista os prestadores em pendência.
///
@RestController
@RequestMapping("/admin/prestadores")
@RequiredArgsConstructor
public class PrestadorAdminController {
    private final PrestadorService service;

    @PatchMapping("/{id}/aprovar")
    @PreAuthorize("hasAuthority('admin::manage')")
    public ResponseEntity<Void> aprovar(@PathVariable Long id) {
        service.aprovar(id);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}/rejeitar")
    @PreAuthorize("hasAuthority('admin::manage')")
    public ResponseEntity<Void> rejeitar(@PathVariable Long id) {
        service.rejeitar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAuthority('admin::manage')")
    public ResponseEntity<List<PrestadorResponse>> pendentes() {
        return ResponseEntity.ok(service.pendentes());
    }
}