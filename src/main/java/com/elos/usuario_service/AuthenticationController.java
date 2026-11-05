package com.elos.usuario_service;

import com.elos.usuario_service.dto.LoginRequest;
import com.elos.usuario_service.dto.LoginResponse;
import com.elos.usuario_service.dto.RegisterRequest;
import com.elos.usuario_service.dto.RegisterResponse;
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

