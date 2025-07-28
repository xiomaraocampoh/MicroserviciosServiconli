package com.serviconli.apigateway.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.util.List;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Value("${jwt.secret}")
    private String secret;

    public AuthenticationFilter() {
        super(Config.class);
    }

    private static final List<String> PUBLIC_ROUTES = List.of(
            "/auth/register",
            "/auth/login"
    );

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getURI().getPath();

            // Excluir rutas públicas
            if (PUBLIC_ROUTES.stream().anyMatch(path::startsWith)) {
                return chain.filter(exchange);
            }

            // 1. Obtener el header de autorización
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            // 2. Validar el header
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return onError(exchange, HttpStatus.UNAUTHORIZED);
            }

            // 3. Extraer y validar el token
            String token = authHeader.substring(7);
            try {
                byte[] keyBytes = secret.getBytes();
                Key key = Keys.hmacShaKeyFor(keyBytes);
                Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            } catch (Exception e) {
                return onError(exchange, HttpStatus.UNAUTHORIZED);
            }

            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        return exchange.getResponse().setComplete();
    }

    public static class Config {}
}
