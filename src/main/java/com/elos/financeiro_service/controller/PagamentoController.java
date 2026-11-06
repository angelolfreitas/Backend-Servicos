package com.elos.financeiro_service.controller;


import com.elos.financeiro_service.dto.CobrancaRequest;
import com.elos.financeiro_service.dto.PagamentoResponse;
import com.elos.financeiro_service.dto.SaldoResponse;
import com.elos.financeiro_service.service.PagamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pagamentos")
@RequiredArgsConstructor
public class PagamentoController {
    private final PagamentoService service;

    @PostMapping("/{idContratacao}/etapa-inicial")
    @PreAuthorize("hasAuthority('user::write')")
    public ResponseEntity<PagamentoResponse> etapaInicial(@PathVariable Long idContratacao,
                                                          @Valid @RequestBody CobrancaRequest req,
                                                          @AuthenticationPrincipal Long idUsuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.pagarEtapaInicial(idUsuario, idContratacao, req));
    }

    @PostMapping("/{idContratacao}/etapa-final")
    @PreAuthorize("hasAuthority('user::write')")
    public ResponseEntity<PagamentoResponse> etapaFinal(@PathVariable Long idContratacao,
                                                        @Valid @RequestBody CobrancaRequest req,
                                                        @AuthenticationPrincipal Long idUsuario) {
        return ResponseEntity.ok(service.pagarEtapaFinal(idUsuario, idContratacao, req));
    }

    @GetMapping("/contratacao/{idContratacao}")
    @PreAuthorize("hasAuthority('user::read')")
    public ResponseEntity<PagamentoResponse> consultar(@PathVariable Long idContratacao,
                                                       @AuthenticationPrincipal Long idUsuario) {
        return ResponseEntity.ok(service.consultar(idContratacao, idUsuario));
    }

    @GetMapping("/saldo")
    @PreAuthorize("hasAuthority('user::read')")
    public ResponseEntity<SaldoResponse> saldo(@AuthenticationPrincipal Long idUsuario) {
        return ResponseEntity.ok(service.saldo(idUsuario));
    }
}