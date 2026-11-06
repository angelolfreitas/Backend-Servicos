package com.elos.catalogo_service.controller;

import com.elos.catalogo_service.service.PortfolioService;
import com.elos.catalogo_service.dto.catalogo.FotoResponse;
import com.elos.catalogo_service.dto.catalogo.PortfolioRequest;
import com.elos.catalogo_service.dto.catalogo.PortfolioResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/portfolio")
@RequiredArgsConstructor
public class PortfolioController {
    private final PortfolioService service;

    @PutMapping("/meu")
    @PreAuthorize("hasAuthority('user::write')")
    public ResponseEntity<PortfolioResponse> salvar(@RequestBody PortfolioRequest req,
                                                    @AuthenticationPrincipal Long idUsuario) {
        return ResponseEntity.ok(service.salvar(idUsuario, req));
    }

    @GetMapping("/meu")
    @PreAuthorize("hasAuthority('user::read')")
    public ResponseEntity<PortfolioResponse> meu(@AuthenticationPrincipal Long idUsuario) {
        return ResponseEntity.ok(service.meu(idUsuario));
    }

    @GetMapping("/prestador/{idPrestador}")
    @PreAuthorize("hasAuthority('user::read')")
    public ResponseEntity<PortfolioResponse> doPrestador(@PathVariable Long idPrestador) {
        return ResponseEntity.ok(service.doPrestador(idPrestador));
    }

    @PostMapping(value = "/meu/fotos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('user::write')")
    public ResponseEntity<FotoResponse> adicionarFoto(@RequestParam("arquivo") MultipartFile arquivo,
                                                      @RequestParam(required = false) String descricao,
                                                      @AuthenticationPrincipal Long idUsuario) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.adicionarFoto(idUsuario, arquivo, descricao));
    }

    @DeleteMapping("/meu/fotos/{idFoto}")
    @PreAuthorize("hasAuthority('user::delete')")
    public ResponseEntity<Void> removerFoto(@PathVariable Long idFoto,
                                            @AuthenticationPrincipal Long idUsuario) throws IOException {
        service.removerFoto(idUsuario, idFoto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/fotos/{idFoto}/arquivo")
    @PreAuthorize("hasAuthority('user::read')")
    public ResponseEntity<Resource> arquivo(@PathVariable Long idFoto) {
        Resource recurso = service.arquivo(idFoto);
        MediaType tipo = MediaTypeFactory.getMediaType(recurso).orElse(MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok().contentType(tipo).body(recurso);
    }
}