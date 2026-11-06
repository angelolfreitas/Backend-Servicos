package com.elos.catalogo_service.controller;

import com.elos.catalogo_service.dto.catalogo.VitrineItem;
import com.elos.catalogo_service.service.VitrineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/vitrine")
@RequiredArgsConstructor
public class VitrineController {
    private final VitrineService service;

    @GetMapping
    @PreAuthorize("hasAuthority('user::read')")
    public ResponseEntity<List<VitrineItem>> buscar(@RequestParam(required = false) String texto,
                                                    @RequestParam(required = false) Long idCategoria,
                                                    @RequestParam(required = false) BigDecimal notaMinima,
                                                    @RequestParam(defaultValue = "nota") String ordem,
                                                    @RequestParam(defaultValue = "0") int pagina) {
        return ResponseEntity.ok(service.buscar(texto, idCategoria, notaMinima, ordem, pagina));
    }
}