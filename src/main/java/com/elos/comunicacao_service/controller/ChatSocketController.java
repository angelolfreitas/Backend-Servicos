package com.elos.comunicacao_service.controller;

import com.elos.comunicacao_service.dto.MensagemRequest;
import com.elos.comunicacao_service.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatSocketController {
    private final ChatService service;


    @MessageMapping("/chat/{idContratacao}")
    public void enviar(@DestinationVariable Long idContratacao, @Valid @Payload MensagemRequest req,
                       Principal principal) {
        service.enviarTexto(idContratacao, Long.valueOf(principal.getName()), req.texto());
    }

    @MessageExceptionHandler
    @SendToUser("/queue/erros")
    public String erro(Exception e) {
        if (e instanceof MethodArgumentNotValidException v && v.getBindingResult().hasFieldErrors())
            return v.getBindingResult().getFieldErrors().getFirst().getDefaultMessage();
        return e instanceof ResponseStatusException r ? r.getReason() : "Erro ao enviar a mensagem";
    }
}