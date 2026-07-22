package com.msp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.msp.dto.response.ProviderBookingInfoResponse;
import com.msp.dto.response.ProviderIdentityResponse;
import com.msp.service.ProviderInternalService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/providers/internal")
@RequiredArgsConstructor
public class ProviderInternalController {

    private final ProviderInternalService providerInternalService;

    @GetMapping("/{providerId}/categories/{categoryId}/booking-info")
    public ResponseEntity<ProviderBookingInfoResponse> getProviderBookingInfo(
            @PathVariable Long providerId,
            @PathVariable Long categoryId) {

        return ResponseEntity.ok(
                providerInternalService.getProviderBookingInfo(
                        providerId,
                        categoryId
                )
        );
    }

    @GetMapping("/by-auth-user/{authUserId}")
    public ResponseEntity<ProviderIdentityResponse>
            getProviderIdentityByAuthUserId(
                    @PathVariable Long authUserId) {

        return ResponseEntity.ok(
                providerInternalService
                        .getProviderIdentityByAuthUserId(authUserId)
        );
    }
}