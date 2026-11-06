package com.elos.suporte_service.controller;

import com.elos.suporte_service.dto.*;
import com.elos.suporte_service.service.ModeracaoService;
import com.elos.suporte_service.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('admin::manage')")
@RequiredArgsConstructor
public class SuporteAdminController {
    private final TicketService tickets;
    private final ModeracaoService moderacao;

    @GetMapping("/tickets")
    public ResponseEntity<List<TicketResponse>> fila(@RequestParam(required = false) String status) {
        return ResponseEntity.ok(tickets.fila(status));
    }

    @GetMapping("/tickets/{id}")
    public ResponseEntity<TicketResponse> detalhe(@PathVariable Long id) {
        return ResponseEntity.ok(tickets.detalheAdmin(id));
    }

    @PostMapping("/tickets/{id}/mensagens")
    public ResponseEntity<TicketResponse> responder(@PathVariable Long id,
                                                    @Valid @RequestBody MensagemTicketRequest req,
                                                    @AuthenticationPrincipal Long idAdmin) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tickets.responderAdmin(id, idAdmin, req.mensagem()));
    }

    @PatchMapping("/tickets/{id}/encerrar")
    public ResponseEntity<TicketResponse> encerrar(@PathVariable Long id, @AuthenticationPrincipal Long idAdmin) {
        return ResponseEntity.ok(tickets.encerrar(id, idAdmin));
    }

    @PostMapping("/moderacoes/contas")
    public ResponseEntity<ModeracaoResponse> conta(@Valid @RequestBody ModeracaoContaRequest req,
                                                   @AuthenticationPrincipal Long idAdmin) {
        return ResponseEntity.status(HttpStatus.CREATED).body(moderacao.moderarConta(idAdmin, req));
    }

    @PostMapping("/moderacoes/avaliacoes")
    public ResponseEntity<ModeracaoResponse> avaliacao(@Valid @RequestBody ModeracaoAvaliacaoRequest req,
                                                       @AuthenticationPrincipal Long idAdmin) {
        return ResponseEntity.status(HttpStatus.CREATED).body(moderacao.moderarAvaliacao(idAdmin, req));
    }

    @GetMapping("/moderacoes")
    public ResponseEntity<List<ModeracaoResponse>> historico() {
        return ResponseEntity.ok(moderacao.recentes());
    }
}