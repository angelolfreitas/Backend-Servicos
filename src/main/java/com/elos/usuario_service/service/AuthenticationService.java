package com.elos.usuario_service.service;

import com.elos.usuario_service.dto.LoginRequest;
import com.elos.usuario_service.dto.LoginResponse;
import com.elos.usuario_service.dto.RegisterRequest;
import com.elos.usuario_service.dto.RegisterResponse;
import com.elos.usuario_service.repository.UsuarioRepository;
import com.elos.usuario_service.infra.security.TokenService;
import com.elos.usuario_service.usuario.Usuario;
import com.elos.usuario_service.usuario.role.Role;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class AuthenticationService {
    private final UsuarioRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    private static final Set<String> CAMPOS_EDITAVEIS = Set.of("username", "telefone");

    public Optional<RegisterResponse> register(RegisterRequest register, Role role) {
        if (userRepository.findByEmail(normalizar(register.email())).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado");
        return Optional.of(buildUser(register, role));
    }

    private RegisterResponse buildUser(RegisterRequest register, Role role) {
        Usuario user = Usuario.builder()
                .username(register.username().strip())
                .email(normalizar(register.email()))
                .password(passwordEncoder.encode(register.password()))
                .telefone(register.telefone())
                .tipoConta(register.tipoConta())
                .statusConta("ATIVO")
                .dataCadastro(LocalDate.now())
                .role(role)
                .build();

        userRepository.save(user);
        return new RegisterResponse(user.getUsername(), tokenService.generateToken(user));
    }
    private String normalizar(String email) {
        return email.strip().toLowerCase(Locale.ROOT);
    }

    public boolean updateRole(Usuario user, Role newRole) {
        if (user.getRole() == newRole)
            return false;

        user.setRole(newRole);
        this.userRepository.save(user);
        return true;
    }


    public Optional<LoginResponse> login(LoginRequest login) {
        Usuario user = userRepository.findByEmail(normalizar(login.email()))
                .filter(u -> passwordEncoder.matches(login.password(), u.getPassword()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas"));

        if (user.estaBloqueado())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Conta suspensa ou banida");

        String token = tokenService.generateToken(user);
        return Optional.of(new LoginResponse(user.getEmail(), token, user.getRole().toString()));
    }

    public Optional<RegisterResponse> registerManager(RegisterRequest register) {
        String email = normalizar(register.email());
        Optional<Usuario> existente = userRepository.findByEmail(email);
        if (existente.isEmpty())
            return Optional.of(buildUser(register, Role.MANAGER));

        Usuario user = existente.get();
        if (!updateRole(user, Role.MANAGER))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Usuário já é manager");
        return Optional.of(new RegisterResponse(user.getUsername(), tokenService.generateToken(user)));
    }

    @Transactional
    public RegisterResponse updateUsuario(RegisterRequest update, Usuario user) {
        String email = normalizar(update.email());
        if (!email.equals(user.getEmail()) && userRepository.findByEmail(email).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado");

        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(update.password()));
        user.setUsername(update.username().strip());
        userRepository.save(user);
        return new RegisterResponse(user.getUsername(), tokenService.generateToken(user));
    }

    @Transactional
    public void patchUser(Usuario user, Map<String, Object> updates) {
        updates.forEach((campo, valor) -> {
            if (!CAMPOS_EDITAVEIS.contains(campo))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Campo não editável: " + campo);
            if (!(valor instanceof String texto) || texto.isBlank())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valor inválido para " + campo);
            switch (campo) {
                case "username" -> user.setUsername(texto.strip());
                case "telefone" -> user.setTelefone(texto.strip());
            }
        });
        userRepository.save(user);
    }

}

