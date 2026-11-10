package com.elos.usuario_service.infra.security;


import com.elos.usuario_service.repository.UsuarioRepository;
import com.elos.usuario_service.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
/// Implementacao da classe user details para passar as credenciais do usuario específicas do nosso siarwm.
@Component
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UsuarioRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario user = userRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException(username));

        return new org.springframework.security.core.userdetails.User(username,
                user.getPassword(),
                user.getAuthorities());
    }
}
