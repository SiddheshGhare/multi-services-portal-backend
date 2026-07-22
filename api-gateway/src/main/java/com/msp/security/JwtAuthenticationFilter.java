package com.msp.security;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
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

        String path = exchange.getRequest()
                .getURI()
                .getPath();

        HttpMethod method = exchange.getRequest()
                .getMethod();

        /*
         * Allow browser CORS preflight requests.
         */
        if (HttpMethod.OPTIONS.equals(method)) {
            return chain.filter(exchange);
        }

        /*
         * Public Auth Service endpoints:
         *
         * /api/auth/register
         * /api/auth/login
         * /api/auth/refresh
         * /api/auth/logout
         */
        if (isAuthPublicPath(path)) {
            return chain.filter(exchange);
        }

        /*
         * Public provider search endpoint.
         *
         * Only GET is public.
         *
         * Examples:
         * GET /api/providers/search
         * GET /api/providers/search?categoryId=1
         */
        if (isPublicProviderSearch(path, method)) {
            return chain.filter(exchange);
        }

        /*
         * Provider internal endpoints must not be called
         * by frontend users through the API Gateway.
         *
         * Booking Service calls these endpoints directly
         * using OpenFeign and Eureka.
         */
        if (isProviderInternalPath(path)) {
            return writeErrorResponse(
                    exchange,
                    HttpStatus.FORBIDDEN,
                    "Internal API cannot be accessed through API Gateway"
            );
        }

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        /*
         * Every non-public endpoint must contain:
         *
         * Authorization: Bearer <token>
         */
        if (authHeader == null
                || !authHeader.startsWith("Bearer ")) {

            return writeErrorResponse(
                    exchange,
                    HttpStatus.UNAUTHORIZED,
                    "Authorization token is missing"
            );
        }

        String token = authHeader.substring(7);

        if (!jwtUtil.validateToken(token)) {

            return writeErrorResponse(
                    exchange,
                    HttpStatus.UNAUTHORIZED,
                    "Invalid or expired access token"
            );
        }

        Long userId;
        String email;
        String role;

        try {

            userId = jwtUtil.extractUserId(token);
            email = jwtUtil.extractEmail(token);
            role = jwtUtil.extractRole(token);

        } catch (Exception exception) {

            return writeErrorResponse(
                    exchange,
                    HttpStatus.UNAUTHORIZED,
                    "Unable to read token details"
            );
        }

        /*
         * Verify whether the authenticated user's role
         * is allowed to access the requested booking API.
         */
        if (!isAuthorized(path, role)) {

            return writeErrorResponse(
                    exchange,
                    HttpStatus.FORBIDDEN,
                    "You are not authorized to access this resource"
            );
        }

        /*
         * Remove any X-User headers supplied manually
         * by the client.
         *
         * Then add trusted headers extracted from JWT.
         */
        ServerWebExchange modifiedExchange = exchange.mutate()
                .request(requestBuilder -> requestBuilder.headers(headers -> {

                    headers.remove("X-User-Id");
                    headers.remove("X-User-Email");
                    headers.remove("X-User-Role");

                    headers.set(
                            "X-User-Id",
                            String.valueOf(userId)
                    );

                    headers.set(
                            "X-User-Email",
                            email
                    );

                    headers.set(
                            "X-User-Role",
                            role
                    );
                }))
                .build();

        return chain.filter(modifiedExchange);
    }

    /*
     * Public Auth APIs.
     */
    private boolean isAuthPublicPath(String path) {

        return path.equals("/api/auth")
                || path.startsWith("/api/auth/");
    }

    /*
     * Only GET /api/providers/search is public.
     *
     * PUT, POST or DELETE requests with the same path
     * will still require authentication.
     */
    private boolean isPublicProviderSearch(
            String path,
            HttpMethod method) {

        return HttpMethod.GET.equals(method)
                && path.equals("/api/providers/search");
    }

    /*
     * These APIs are only for internal communication
     * between microservices.
     */
    private boolean isProviderInternalPath(String path) {

        return path.equals("/api/providers/internal")
                || path.startsWith("/api/providers/internal/");
    }

    /*
     * Role-based booking authorization.
     */
    private boolean isAuthorized(
            String path,
            String role) {

        if (role == null || role.isBlank()) {
            return false;
        }

        /*
         * Handles both:
         *
         * ADMIN
         * ROLE_ADMIN
         */
        String normalizedRole = role.startsWith("ROLE_")
                ? role.substring(5)
                : role;

        if (isPathOrChild(path, "/api/admin/bookings")) {
            return "ADMIN".equalsIgnoreCase(normalizedRole);
        }

        if (isPathOrChild(path, "/api/provider/bookings")) {
            return "PROVIDER".equalsIgnoreCase(normalizedRole);
        }

        if (isPathOrChild(path, "/api/bookings")) {
            return "CUSTOMER".equalsIgnoreCase(normalizedRole);
        }

        /*
         * Other existing APIs require a valid token,
         * but currently have no Gateway role restriction.
         */
        return true;
    }

    private boolean isPathOrChild(
            String path,
            String basePath) {

        return path.equals(basePath)
                || path.startsWith(basePath + "/");
    }

    private Mono<Void> writeErrorResponse(
            ServerWebExchange exchange,
            HttpStatus status,
            String message) {

        ServerHttpResponse response = exchange.getResponse();

        response.setStatusCode(status);

        response.getHeaders().setContentType(
                MediaType.APPLICATION_JSON
        );

        String responseBody = """
                {
                  "status": %d,
                  "error": "%s",
                  "message": "%s",
                  "path": "%s"
                }
                """.formatted(
                status.value(),
                status.getReasonPhrase(),
                escapeJson(message),
                escapeJson(
                        exchange.getRequest()
                                .getURI()
                                .getPath()
                )
        );

        DataBuffer buffer = response.bufferFactory()
                .wrap(
                        responseBody.getBytes(
                                StandardCharsets.UTF_8
                        )
                );

        return response.writeWith(Mono.just(buffer));
    }

    private String escapeJson(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }

    @Override
    public int getOrder() {
        return -1;
    }
}