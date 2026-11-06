package com.elos.comunicacao_service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.elos.comunicacao_service.infra.security.Role;
import com.elos.comunicacao_service.service.ChatService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class StompAuthInterceptor implements ChannelInterceptor {
    private static final Pattern TOPICO = Pattern.compile("^/topic/contratacao/(\\d+)$");

    @Value("${api.security.token.secret}")
    private String secret;

    private final ChatService chat;

    // @Lazy evita dependência circular: o broker depende da config, que depende deste interceptor
    public StompAuthInterceptor(@Lazy ChatService chat) {
        this.chat = chat;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor acc = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (acc == null || acc.getCommand() == null) return message;

        switch (acc.getCommand()) {
            case CONNECT -> acc.setUser(autenticar(acc.getFirstNativeHeader("Authorization")));
            case SUBSCRIBE -> {
                Long idUsuario = idDe(acc);
                String destino = acc.getDestination();
                if (destino != null && destino.startsWith("/topic/")) {
                    Matcher m = TOPICO.matcher(destino);
                    if (!m.matches()) throw new MessagingException("Destino não permitido");
                    chat.autorizarInscricao(Long.valueOf(m.group(1)), idUsuario);
                }
            }
            case SEND -> {
                idDe(acc);
                String destino = acc.getDestination();
                if (destino == null || !destino.startsWith("/app/"))
                    throw new MessagingException("Destino não permitido");
            }
            default -> { }
        }
        return message;
    }

    private Authentication autenticar(String header) {
        if (header == null || !header.startsWith("Bearer "))
            throw new MessagingException("Token ausente");
        try {
            DecodedJWT jwt = JWT.require(Algorithm.HMAC256(secret)).withIssuer("elos_api")
                    .build().verify(header.substring(7));
            Role role = Role.valueOf(jwt.getClaim("role").asString());
            return new UsernamePasswordAuthenticationToken(
                    jwt.getClaim("userId").asLong(), null, role.getAuthorities());
        } catch (RuntimeException e) {
            throw new MessagingException("Token inválido ou expirado");
        }
    }

    private Long idDe(StompHeaderAccessor acc) {
        if (acc.getUser() == null) throw new MessagingException("Não autenticado");
        return Long.valueOf(acc.getUser().getName());
    }
}