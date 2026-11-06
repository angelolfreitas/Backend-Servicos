package com.elos.comunicacao_service.controller;

import com.elos.comunicacao_service.service.ChatService;
import com.elos.comunicacao_service.dto.MensagemRequest;
import com.elos.comunicacao_service.dto.MensagemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatService service;

    @GetMapping("/chats/{id}/mensagens")
    @PreAuthorize("hasAuthority('user::read')")
    public ResponseEntity<List<MensagemResponse>> historico(@PathVariable Long id,
                                                            @AuthenticationPrincipal Long idUsuario) {
        return ResponseEntity.ok(service.historico(id, idUsuario, false));
    }

    @PostMapping("/chats/{id}/mensagens")
    @PreAuthorize("hasAuthority('user::write')")
    public ResponseEntity<MensagemResponse> enviar(@PathVariable Long id, @RequestBody MensagemRequest req,
                                                   @AuthenticationPrincipal Long idUsuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.enviarTexto(id, idUsuario, req.texto()));
    }

    @PostMapping(value = "/chats/{id}/anexos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('user::write')")
    public ResponseEntity<MensagemResponse> anexo(@PathVariable Long id,
                                                  @RequestParam("arquivo") MultipartFile arquivo,
                                                  @RequestParam(required = false) String texto,
                                                  @AuthenticationPrincipal Long idUsuario) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.enviarAnexo(id, idUsuario, arquivo, texto));
    }

    @GetMapping("/chats/anexos/{idAnexo}/arquivo")
    @PreAuthorize("hasAuthority('user::read')")
    public ResponseEntity<Resource> arquivo(@PathVariable Long idAnexo,
                                            @AuthenticationPrincipal Long idUsuario, Authentication auth) {
        boolean admin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        Resource r = service.arquivo(idAnexo, idUsuario, admin);
        MediaType tipo = MediaTypeFactory.getMediaType(r).orElse(MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok().contentType(tipo).body(r);
    }

    // RF18: o admin lê o histórico de qualquer chat
    @GetMapping("/admin/chats/{id}/mensagens")
    @PreAuthorize("hasAuthority('admin::manage')")
    public ResponseEntity<List<MensagemResponse>> historicoAdmin(@PathVariable Long id) {
        return ResponseEntity.ok(service.historico(id, null, true));
    }
}
