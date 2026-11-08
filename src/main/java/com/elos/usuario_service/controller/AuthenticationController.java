package com.elos.usuario_service.controller;

import com.elos.usuario_service.dto.*;
import com.elos.usuario_service.service.AuthenticationService;
import com.elos.usuario_service.service.PasswordResetService;
import com.elos.usuario_service.usuario.role.Role;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {
    AuthenticationService authenticationService;
    private final PasswordResetService passwordResetService; // @AllArgsConstructor injeta

    @PostMapping("/forgot-password")
    ResponseEntity<Void> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        passwordResetService.solicitar(request.email());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        passwordResetService.redefinir(request.token(), request.password());
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/sign-up")
    ResponseEntity<RegisterResponse> signUp(@Valid @RequestBody RegisterRequest registerRequest) {
        Optional<RegisterResponse> registerResponse = authenticationService.register(registerRequest,
                Role.USER);
        return registerResponse.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }
    @PostMapping("/sign-in")
    ResponseEntity<LoginResponse> signIn(@Valid @RequestBody LoginRequest loginRequest) {
        Optional<LoginResponse> loginResponse = authenticationService.login(loginRequest);
        return loginResponse.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }
}

