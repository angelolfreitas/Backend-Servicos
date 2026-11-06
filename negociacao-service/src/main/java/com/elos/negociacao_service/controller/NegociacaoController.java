package com.elos.negociacao_service.controller;

import com.elos.negociacao_service.service.NegociacaoService;
import com.elos.negociacao_service.dto.AceiteRequest;
import com.elos.negociacao_service.dto.ContratacaoResponse;
import com.elos.negociacao_service.dto.PropostaRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NegociacaoController {
    private final NegociacaoService service;

    @PostMapping("/propostas")
    @PreAuthorize("hasAuthority('user::write')")
    public ResponseEntity<ContratacaoResponse> propor(@RequestBody PropostaRequest req,
                                                      @AuthenticationPrincipal Long idUsuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.propor(idUsuario, req));
    }

    @GetMapping("/contratacoes")
    @PreAuthorize("hasAuthority('user::read')")
    public ResponseEntity<List<ContratacaoResponse>> minhas(@AuthenticationPrincipal Long idUsuario) {
        return ResponseEntity.ok(service.minhas(idUsuario));
    }

    @GetMapping("/contratacoes/{id}")
    @PreAuthorize("hasAuthority('user::read')")
    public ResponseEntity<ContratacaoResponse> detalhar(@PathVariable Long id,
                                                        @AuthenticationPrincipal Long idUsuario) {
        return ResponseEntity.ok(service.detalhar(id, idUsuario));
    }

    @PostMapping("/contratacoes/{id}/aceitar")
    @PreAuthorize("hasAuthority('user::write')")
    public ResponseEntity<ContratacaoResponse> aceitar(@PathVariable Long id, @RequestBody AceiteRequest req,
                                                       @AuthenticationPrincipal Long idUsuario) {
        return ResponseEntity.ok(service.aceitar(id, idUsuario, req));
    }

    @PostMapping("/contratacoes/{id}/recusar")
    @PreAuthorize("hasAuthority('user::write')")
    public ResponseEntity<ContratacaoResponse> recusar(@PathVariable Long id,
                                                       @AuthenticationPrincipal Long idUsuario) {
        return ResponseEntity.ok(service.recusar(id, idUsuario));
    }

    @PostMapping("/contratacoes/{id}/cancelar")
    @PreAuthorize("hasAuthority('user::write')")
    public ResponseEntity<ContratacaoResponse> cancelar(@PathVariable Long id,
                                                        @AuthenticationPrincipal Long idUsuario) {
        return ResponseEntity.ok(service.cancelar(id, idUsuario));
    }

    @PostMapping("/contratacoes/{id}/concluir")
    @PreAuthorize("hasAuthority('user::write')")
    public ResponseEntity<ContratacaoResponse> concluir(@PathVariable Long id,
                                                        @AuthenticationPrincipal Long idUsuario) {
        return ResponseEntity.ok(service.concluir(id, idUsuario));
    }
}