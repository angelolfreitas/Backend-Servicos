package com.elos.usuario_service.controller;

import com.elos.usuario_service.service.DocumentoService;
import com.elos.usuario_service.service.PrestadorService;
import com.elos.usuario_service.dto.CandidaturaRequest;
import com.elos.usuario_service.dto.DocumentoResponse;
import com.elos.usuario_service.dto.PrestadorResponse;
import com.elos.usuario_service.usuario.Usuario;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/prestadores")
@RequiredArgsConstructor
public class PrestadorController {
    private final PrestadorService service;
    private final DocumentoService documentos;

    @PostMapping(value = "/candidatura/documentos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('user::write')")
    public ResponseEntity<DocumentoResponse> enviarDocumento(@RequestParam String tipoDocumento,
                                                             @RequestParam("arquivo") MultipartFile arquivo,
                                                             @AuthenticationPrincipal Usuario usuario) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(documentos.enviar(usuario.getIdUsuario(), tipoDocumento, arquivo));
    }

    @GetMapping("/candidatura/documentos")
    @PreAuthorize("hasAuthority('user::read')")
    public ResponseEntity<List<DocumentoResponse>> meusDocumentos(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(documentos.meus(usuario.getIdUsuario()));
    }

    @PostMapping("/candidatura")
    @PreAuthorize("hasAuthority('user::write')")
    public ResponseEntity<PrestadorResponse> candidatar(@Valid @RequestBody CandidaturaRequest req,
                                                        @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.candidatar(usuario.getIdUsuario(), req));
    }

    @GetMapping("/candidatura")
    @PreAuthorize("hasAuthority('user::read')")
    public ResponseEntity<PrestadorResponse> minha(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(service.minhaCandidatura(usuario.getIdUsuario()));
    }
}