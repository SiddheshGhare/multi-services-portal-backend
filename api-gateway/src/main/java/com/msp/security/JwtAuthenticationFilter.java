package com.msp.security;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter
        implements GlobalFilter, Ordered {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain) {

        String path =
                exchange.getRequest()
                        .getURI()
                        .getPath();

        // Public APIs
        if (path.startsWith("/api/auth")) {

            return chain.filter(exchange);
        }
        
        

        String authHeader =
                exchange.getRequest()
                        .getHeaders()
                        .getFirst(HttpHeaders.AUTHORIZATION);
        

        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            exchange.getResponse()
                    .setStatusCode(
                            HttpStatus.UNAUTHORIZED);

            return exchange.getResponse()
                    .setComplete();
        }

        String token =
                authHeader.substring(7);
       

        if (!jwtUtil.validateToken(token)) {

            exchange.getResponse()
                    .setStatusCode(
                            HttpStatus.UNAUTHORIZED);

            return exchange.getResponse()
                    .setComplete();
        }

        Long userId =
                jwtUtil.extractUserId(token);

        String email =
                jwtUtil.extractEmail(token);

        String role =
                jwtUtil.extractRole(token);
       

        ServerWebExchange modifiedExchange =
                exchange.mutate()

                        .request(builder -> builder

                                .header(
                                        "X-User-Id",
                                        String.valueOf(userId))

                                .header(
                                        "X-User-Email",
                                        email)

                                .header(
                                        "X-User-Role",
                                        role))

                        .build();

        return chain.filter(modifiedExchange);
    }

    @Override
    public int getOrder() {

        return -1;
    }
}
