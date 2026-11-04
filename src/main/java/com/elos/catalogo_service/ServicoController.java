package com.elos.catalogo_service;


import com.elos.catalogo_service.dto.catalogo.ServicoRequest;
import com.elos.catalogo_service.dto.catalogo.ServicoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicos")
@RequiredArgsConstructor
public class ServicoController {

    private final ServicoService servicoService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('user::read')")
    public ResponseEntity<List<ServicoResponse>> listarTodos() {
        return ResponseEntity.ok(
                servicoService.listarTodos()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('user::read')")
    public ResponseEntity<ServicoResponse> buscarPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                servicoService.wrapPorId(id)
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('user::write')")
    public ResponseEntity<ServicoResponse> criar(@RequestBody ServicoRequest servico,
                                                 @AuthenticationPrincipal Long idUsuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicoService.criar(servico, idUsuario));
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('user::write')")
    public ResponseEntity<ServicoResponse> atualizar(
            @PathVariable Long id,
            @RequestBody ServicoRequest servico,
            @AuthenticationPrincipal Long idUsuario
    ) {
        return ResponseEntity.ok(servicoService.atualizar(id, servico, idUsuario));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('user::delete')")
    public ResponseEntity<Void> excluir(
            @PathVariable Long id,
            @AuthenticationPrincipal Long idUsuario
    ) {
        servicoService.excluir(id, idUsuario);
        return ResponseEntity.noContent().build();
    }
}