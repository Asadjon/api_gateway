package org.cyber_pantera.filter;

import lombok.RequiredArgsConstructor;
import org.cyber_pantera.dto.UserResponse;
import org.cyber_pantera.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends AbstractGatewayFilterFactory<Object>{

    private final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    private final AuthService authService;

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) ->  {
            ServerHttpRequest request = exchange.getRequest();
            var token = extractToken(request);
            if (token == null)
                return handleAuthenticationError(exchange, "Invalid token");

            logger.info("Token: {}", token);
            return authService.validateToken(token)
                    .flatMap(user -> proceedWithUser(user, exchange, chain))
                    .onErrorResume(ResponseStatusException.class,
                            e -> handleAuthenticationError(exchange, e.getMessage()));
        };
    }

    private String extractToken(ServerHttpRequest request) {
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        return authHeader != null && authHeader.startsWith("Bearer ") ? authHeader.substring(7) : null;
    }

    private Mono<Void> proceedWithUser(UserResponse user, ServerWebExchange exchange, GatewayFilterChain chain) {
        var request = exchange.getRequest().mutate()
                .header("x-User-Id", String.valueOf(user.getId()))
                .build();

        return chain.filter(exchange.mutate().request(request).build());
    }

    private Mono<Void> handleAuthenticationError(ServerWebExchange exchange, String message) {
        logger.warn("Token validation failed: {}", message);
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
}
