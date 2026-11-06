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

@RequiredArgsConstructor
@Service
public class AuthenticationService {
    private final UsuarioRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public Optional<LoginResponse> login(LoginRequest login) {
        Usuario user = userRepository.findByEmail(normalizar(login.email()))
                .filter(u -> passwordEncoder.matches(login.password(), u.getPassword()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas"));

        if (user.estaBloqueado())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Conta suspensa ou banida");

        String token = tokenService.generateToken(user);
        return Optional.of(new LoginResponse(user.getEmail(), token, user.getRole().toString()));
    }
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
                .statusConta("ATIVO")              // o servidor decide, nunca o cliente
                .dataCadastro(LocalDate.now())
                .role(role)
                .build();

        userRepository.save(user);
        return new RegisterResponse(user.getUsername(), tokenService.generateToken(user));
    }
    private String normalizar(String email) {
        return email.strip().toLowerCase(Locale.ROOT);
    }

    public Optional<RegisterResponse> registerManager(RegisterRequest register) {
        Optional<Usuario> verifyUser = this.userRepository.findByEmail(register.email());
        //usuario inexistente, criado como manager
        if(verifyUser.isEmpty())
            return Optional.of(buildUser(register, Role.MANAGER));
        //usuário existente, atualiza a role
        Usuario user = verifyUser.get();
        //retorna nada se o usuário já é manager
        if(!updateRole(user, Role.MANAGER))
            throw new RuntimeException(register.username());


        String newToken = tokenService.generateToken(user);
        return Optional.of(new RegisterResponse(user.getUsername(), newToken));
    }

    public boolean updateRole(Usuario user, Role newRole) {
        if (user.getRole() == newRole)
            return false;

        user.setRole(newRole);
        this.userRepository.save(user);
        return true;
    }


    @Transactional
    public RegisterResponse updateUsuario(RegisterRequest update, Usuario user) {
        user.setEmail(update.email());
        user.setPassword(passwordEncoder.encode(update.password()));
        user.setUsername(update.username());

        this.userRepository.save(user);
        String newToken = tokenService.generateToken(user);
        return new RegisterResponse(user.getUsername(), newToken);
    }
    @Transactional
    public void patchUser(Usuario user, Map<String, Object> updates) {
        updates.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Usuario.class, key);
            field.setAccessible(true);
            ReflectionUtils.setField(field, user, value);
        });
        this.userRepository.save(user);
    }

}

