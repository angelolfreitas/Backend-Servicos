package com.elos.suporte_service.controller;

import com.elos.suporte_service.dto.MensagemTicketRequest;
import com.elos.suporte_service.dto.TicketRequest;
import com.elos.suporte_service.dto.TicketResponse;
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
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService service;

    @PostMapping
    @PreAuthorize("hasAuthority('user::write')")
    public ResponseEntity<TicketResponse> abrir(@Valid @RequestBody TicketRequest req,
                                                @AuthenticationPrincipal Long idUsuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.abrir(idUsuario, req));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('user::read')")
    public ResponseEntity<List<TicketResponse>> meus(@AuthenticationPrincipal Long idUsuario) {
        return ResponseEntity.ok(service.meus(idUsuario));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('user::read')")
    public ResponseEntity<TicketResponse> detalhe(@PathVariable Long id, @AuthenticationPrincipal Long idUsuario) {
        return ResponseEntity.ok(service.detalheDoUsuario(id, idUsuario));
    }

    @PostMapping("/{id}/mensagens")
    @PreAuthorize("hasAuthority('user::write')")
    public ResponseEntity<TicketResponse> responder(@PathVariable Long id,
                                                    @Valid @RequestBody MensagemTicketRequest req,
                                                    @AuthenticationPrincipal Long idUsuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.responderUsuario(id, idUsuario, req.mensagem()));
    }
}