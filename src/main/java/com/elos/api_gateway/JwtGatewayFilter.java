package com.elos.api_gateway;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpHeaders;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class JwtGatewayFilter implements GlobalFilter, Ordered {

    private record RotaPublica(HttpMethod metodo, PathPattern padrao) {}

    private final JWTVerifier verifier;
    private final ContasBloqueadas bloqueadas;
    private final List<RotaPublica> publicas;

    public JwtGatewayFilter(@Value("${api.security.token.secret}") String secret,
                            @Value("${gateway.rotas-publicas}") List<String> rotasPublicas,
                            ContasBloqueadas bloqueadas) {
        this.verifier = JWT.require(Algorithm.HMAC256(secret)).build();
        this.bloqueadas = bloqueadas;
        PathPatternParser parser = new PathPatternParser();
        this.publicas = rotasPublicas.stream().map(r -> {
            String[] partes = r.trim().split("\\s+", 2);
            return new RotaPublica(HttpMethod.valueOf(partes[0]), parser.parse(partes[1]));
        }).toList();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest req = exchange.getRequest();

        if (HttpMethod.OPTIONS.equals(req.getMethod()) || ehPublica(req))
            return chain.filter(exchange);

        String header = req.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer "))
            return recusar(exchange, HttpStatus.UNAUTHORIZED, "Token ausente");

        try {
            DecodedJWT jwt = verifier.verify(header.substring(7));
            Long userId = jwt.getClaim("userId").asLong();
            if (userId != null && bloqueadas.contem(userId.toString()))
                return recusar(exchange, HttpStatus.FORBIDDEN, "Conta suspensa ou banida");
            return chain.filter(exchange);
        } catch (JWTVerificationException e) {
            return recusar(exchange, HttpStatus.UNAUTHORIZED, "Token inválido ou expirado");
        }
    }

    private boolean ehPublica(ServerHttpRequest req) {
        PathContainer caminho = req.getPath().pathWithinApplication();
        return publicas.stream().anyMatch(r -> r.metodo().equals(req.getMethod()) && r.padrao().matches(caminho));
    }

    private Mono<Void> recusar(ServerWebExchange exchange, HttpStatus status, String mensagem) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String json = "{\"status\":" + status.value() + ",\"mensagem\":\"" + mensagem + "\"}";
        DataBuffer corpo = exchange.getResponse().bufferFactory().wrap(json.getBytes(StandardCharsets.UTF_8));
        return exchange.getResponse().writeWith(Mono.just(corpo));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}