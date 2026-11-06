package com.elos.dashboard_service.controller;


import com.elos.dashboard_service.dto.FaturamentoDiaResponse;
import com.elos.dashboard_service.dto.ResumoResponse;
import com.elos.dashboard_service.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService service;

    @GetMapping("/resumo")
    @PreAuthorize("hasAuthority('admin::read')")
    public ResponseEntity<ResumoResponse> resumo() {
        return ResponseEntity.ok(service.resumo());
    }

    @GetMapping("/faturamento")
    @PreAuthorize("hasAuthority('admin::read')")
    public ResponseEntity<List<FaturamentoDiaResponse>> faturamento(@RequestParam(defaultValue = "30") int dias) {
        return ResponseEntity.ok(service.faturamento(dias));
    }
}