package com.elos.api.security;
import com.elos.api.entity.usuario.Usuario;
import com.elos.api.repository.usuario.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//estudar filter depois
@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    private UsuarioRepository userRepository;
    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();

        if (path.startsWith("/oauth2") || path.startsWith("/login")) {
            filterChain.doFilter(request, response);
            return;
        }


        var token  = this.recover(request);
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        var login = tokenService.validateToken(token);
        if (login == null) {
            filterChain.doFilter(request, response);
            return;
        }
        var optionalUser = userRepository.findByEmail(login);
        if (optionalUser.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }
        Usuario user = optionalUser.get();
        var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);


        filterChain.doFilter(request,response);
    }
    private String recover(HttpServletRequest request){
        var authHeader =  request.getHeader("Authorization");
        if(authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}
