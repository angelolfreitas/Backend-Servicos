package com.elos.reputacao_service.controller;

import com.elos.reputacao_service.dto.AvaliacaoRequest;
import com.elos.reputacao_service.dto.AvaliacaoResponse;
import com.elos.reputacao_service.dto.ReputacaoResponse;
import com.elos.reputacao_service.service.AvaliacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AvaliacaoController {
    private final AvaliacaoService service;

    @PostMapping("/avaliacoes")
    @PreAuthorize("hasAuthority('user::write')")
    public ResponseEntity<AvaliacaoResponse> avaliar(@Valid @RequestBody AvaliacaoRequest req,
                                                     @AuthenticationPrincipal Long idUsuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.avaliar(idUsuario, req));
    }

    @GetMapping("/avaliacoes/contratacao/{idContratacao}")
    @PreAuthorize("hasAuthority('user::read')")
    public ResponseEntity<List<AvaliacaoResponse>> daContratacao(@PathVariable Long idContratacao,
                                                                 @AuthenticationPrincipal Long idUsuario) {
        return ResponseEntity.ok(service.daContratacao(idContratacao, idUsuario));
    }

    // RF13: painel de confiança (média exata + total)
    @GetMapping("/reputacao/{idUsuario}")
    @PreAuthorize("hasAuthority('user::read')")
    public ResponseEntity<ReputacaoResponse> reputacao(@PathVariable Long idUsuario,
                                                       @RequestParam(defaultValue = "PRESTADOR") String papel) {
        return ResponseEntity.ok(service.reputacao(idUsuario, papel));
    }

    @GetMapping("/reputacao/{idUsuario}/avaliacoes")
    @PreAuthorize("hasAuthority('user::read')")
    public ResponseEntity<List<AvaliacaoResponse>> avaliacoes(@PathVariable Long idUsuario,
                                                              @RequestParam(defaultValue = "PRESTADOR") String papel,
                                                              @RequestParam(defaultValue = "0") int pagina) {
        return ResponseEntity.ok(service.doUsuario(idUsuario, papel, pagina));
    }

    // RF18
    @GetMapping("/admin/avaliacoes")
    @PreAuthorize("hasAuthority('admin::manage')")
    public ResponseEntity<List<AvaliacaoResponse>> recentes() {
        return ResponseEntity.ok(service.recentes());
    }

    @PatchMapping("/admin/avaliacoes/{id}/ocultar")
    @PreAuthorize("hasAuthority('admin::manage')")
    public ResponseEntity<Void> ocultar(@PathVariable Long id, @AuthenticationPrincipal Long idAdmin) {
        service.moderar(id, idAdmin, true);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/admin/avaliacoes/{id}/exibir")
    @PreAuthorize("hasAuthority('admin::manage')")
    public ResponseEntity<Void> exibir(@PathVariable Long id, @AuthenticationPrincipal Long idAdmin) {
        service.moderar(id, idAdmin, false);
        return ResponseEntity.noContent().build();
    }
}