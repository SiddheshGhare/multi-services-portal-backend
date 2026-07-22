package com.msp.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.msp.dto.response.ProviderBookingInfoResponse;
import com.msp.dto.response.ProviderIdentityResponse;

@FeignClient(name = "PROVIDER-SERVICE")
public interface ProviderClient {

    @GetMapping(
        "/api/providers/internal/{providerId}/categories/{categoryId}/booking-info"
    )
    ProviderBookingInfoResponse getProviderBookingInfo(
            @PathVariable Long providerId,
            @PathVariable Long categoryId
    );

    @GetMapping("/api/providers/internal/by-auth-user/{authUserId}")
    ProviderIdentityResponse getProviderIdentityByAuthUserId(
            @PathVariable Long authUserId
    );
}