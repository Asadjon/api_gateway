package org.cyber_pantera.service;

import lombok.RequiredArgsConstructor;
import org.cyber_pantera.dto.UserResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${auth.service.url}")
    private String authServiceUrl;

    private final WebClient.Builder webClientBuilder;

    public Mono<UserResponse> validateToken(String token) {
        return webClientBuilder.build()
                .get().uri(authServiceUrl + "/api/v1/auth/validate?token=" + token)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(UserResponse.class);
    }
}
