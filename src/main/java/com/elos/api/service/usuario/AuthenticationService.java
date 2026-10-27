package com.elos.api.service.usuario;

import com.elos.api.dto.usuario.*;
import com.elos.api.entity.usuario.Usuario;
import com.elos.api.entity.usuario.role.Role;
import com.elos.api.repository.usuario.UsuarioRepository;
import com.elos.api.security.TokenService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthenticationService {
    private final UsuarioRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public Optional<LoginResponse> login(LoginRequest login) {
        Usuario user = this.userRepository.findByEmail(login.email())
                .orElseThrow(
                        ()->new RuntimeException(login.email()
                        ));
        if(!passwordEncoder.matches(login.password(), user.getPassword()))
            throw new RuntimeException();

        String token = tokenService.generateToken(user);

        return Optional.of(new LoginResponse(login.email(),token, user.getRole().toString()));
    }
    public Optional<RegisterResponse> register(RegisterRequest register, Role role) {
        Optional<Usuario> verifyUser = this.userRepository.findByEmail(register.email());
        if(verifyUser.isPresent())
            throw new RuntimeException(register.email());
        return Optional.of(buildUser(register, role));
    }

    private RegisterResponse buildUser(RegisterRequest register, Role role) {
        Usuario user = Usuario.builder()
                .password(passwordEncoder.encode(register.password()))
                .email(register.email())
                .username(register.username())
                .role(role)
                .build();

        this.userRepository.save(user);

        String token = tokenService.generateToken(user);
        return new RegisterResponse(user.getUsername(),token);
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

