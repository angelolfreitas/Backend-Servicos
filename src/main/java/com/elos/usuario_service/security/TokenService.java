package com.elos.usuario_service.security;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import com.elos.usuario_service.usuario.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;
    public String generateToken(Usuario user){
        Algorithm algorithm;
        try{
            algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("elos_api")
                    .withClaim("userId", user.getIdUsuario())
                    .withClaim("role", user.getRole().name())
                    .withSubject(user.getEmail())
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);
            return token;
        }catch(JWTCreationException e){
            throw new RuntimeException("Error while authenticating");
        }
    }
    public String validateToken(String token){
        Algorithm algorithm;
        try {
            algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("elos_api")
                    .build()
                    .verify(token)
                    .getSubject();
        }catch(JWTVerificationException e){
            return null;
        }
    }
    private Instant generateExpirationDate(){
        return LocalDateTime.now().plusHours(1).toInstant(ZoneOffset.of("-3"));
    }
}
