package com.elos.usuario_service.service;


import com.elos.usuario_service.repository.PasswordResetTokenRepository;
import com.elos.usuario_service.repository.UsuarioRepository;
import com.elos.usuario_service.usuario.PasswordResetToken;
import com.elos.usuario_service.usuario.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.HtmlUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class PasswordResetService {
    private static final Duration VALIDADE = Duration.ofMinutes(30);

    private final UsuarioRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final SecureRandom random = new SecureRandom();

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Transactional
    public void solicitar(String emailBruto) {
        String email = emailBruto.strip().toLowerCase(Locale.ROOT);

        // não revela se o e-mail existe
        userRepository.findByEmail(email).ifPresent(user -> {
            tokenRepository.deleteByEmail(email); // invalida pedidos anteriores

            byte[] bytes = new byte[32];
            random.nextBytes(bytes);
            String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

            tokenRepository.save(PasswordResetToken.builder()
                    .tokenHash(hash(token))
                    .email(email)
                    .expiresAt(Instant.now().plus(VALIDADE))
                    .build());

            String link = frontendUrl + "/redefinir-senha?token=" + token;
            emailService.enviar(email, "Redefinição de senha", """
                    <p>Olá, %s!</p>
                    <p>Recebemos um pedido para redefinir sua senha.
                    <a href="%s">Clique aqui para criar uma nova senha</a>.</p>
                    <p>O link expira em 30 minutos. Se não foi você, ignore este e-mail.</p>
                    """.formatted(HtmlUtils.htmlEscape(user.getUsername()), link));
        });
    }

    @Transactional
    public void redefinir(String token, String novaSenha) {
        PasswordResetToken prt = tokenRepository.findByTokenHash(hash(token))
                .filter(t -> !t.isUsed() && t.getExpiresAt().isAfter(Instant.now()))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Token inválido ou expirado"));

        Usuario user = userRepository.findByEmail(prt.getEmail())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Token inválido ou expirado"));

        user.setPassword(passwordEncoder.encode(novaSenha));
        userRepository.save(user);
        prt.setUsed(true);
        tokenRepository.save(prt);
    }

    private String hash(String token) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}