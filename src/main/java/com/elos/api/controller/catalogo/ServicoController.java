package com.elos.api.controller.catalogo;

import com.elos.api.dto.catalogo.ServicoRequest;
import com.elos.api.dto.catalogo.ServicoResponse;
import com.elos.api.entity.catalogo.Servico;
import com.elos.api.service.catalogo.ServicoService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyAuthority('user::write')")
    public ResponseEntity<ServicoResponse> criar(
            @RequestBody ServicoRequest servico
    ) {
        ServicoResponse novoServico = servicoService.criar(servico);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(novoServico);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('user::write')")
    public ResponseEntity<ServicoResponse> atualizar(
            @PathVariable Long id,
            @RequestBody ServicoRequest servico
    ) {
        return ResponseEntity.ok(
                servicoService.atualizar(id, servico)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('user::delete')")
    public ResponseEntity<Void> excluir(
            @PathVariable Long id
    ) {
        servicoService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}