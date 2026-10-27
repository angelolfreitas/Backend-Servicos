package com.elos.api.controller.usuario;

import com.elos.api.dto.usuario.LoginRequest;
import com.elos.api.dto.usuario.LoginResponse;
import com.elos.api.dto.usuario.RegisterRequest;
import com.elos.api.dto.usuario.RegisterResponse;
import com.elos.api.entity.usuario.role.Role;
import com.elos.api.service.usuario.AuthenticationService;
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
    ResponseEntity<RegisterResponse> signUp(@RequestBody RegisterRequest registerRequest) {
        Optional<RegisterResponse> registerResponse = authenticationService.register(registerRequest,
                Role.USER);
        return registerResponse.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }
    @PostMapping("/sign-in")
    ResponseEntity<LoginResponse> signIn(@RequestBody LoginRequest loginRequest) {
        Optional<LoginResponse> loginResponse = authenticationService.login(loginRequest);
        return loginResponse.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }
}

