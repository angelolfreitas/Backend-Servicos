package com.elos.usuario_service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}