package com.msp.client;

import com.msp.dto.response.AuthUserSummaryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "AUTH-SERVICE")
public interface AuthServiceClient {

    @GetMapping("/api/internal/users/{userId}")
    AuthUserSummaryResponse getUserById(@PathVariable("userId") Long userId);
}
