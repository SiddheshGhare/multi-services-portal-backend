package com.msp.controller;

import com.msp.dto.common.ApiResponse;
import com.msp.dto.request.CreateProviderProfileRequest;
import com.msp.dto.request.UpdateProviderProfileRequest;
import com.msp.dto.response.ProviderProfileResponse;
import com.msp.service.ProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/providers")
@RequiredArgsConstructor
public class ProviderController {

    private final ProviderService service;

    @PostMapping("/profile/createProfile")
    public ApiResponse<ProviderProfileResponse> createProfile(
            @RequestHeader("X-User-Id") Long authUserId,
            @RequestBody CreateProviderProfileRequest request) {

        return ApiResponse.<ProviderProfileResponse>builder()
                .success(true)
                .message("Profile created successfully")
                .data(service.createProfile(authUserId, request))
                .build();
    }

    @GetMapping("/profile/getProfile")
    public ApiResponse<ProviderProfileResponse> getMyProfile(
            @RequestHeader("X-User-Id") Long authUserId) {

        return ApiResponse.<ProviderProfileResponse>builder()
                .success(true)
                .data(service.getMyProfile(authUserId))
                .build();
    }

    @PutMapping("/profile/updateProfile")
    public ApiResponse<ProviderProfileResponse> updateProfile(
            @RequestHeader("X-User-Id") Long authUserId,
            @RequestBody UpdateProviderProfileRequest request) {

        return ApiResponse.<ProviderProfileResponse>builder()
                .success(true)
                .message("Profile updated successfully")
                .data(service.updateProfile(authUserId, request))
                .build();
    }

    @GetMapping("/getProviderById/{providerId}")
    public ApiResponse<ProviderProfileResponse> getProviderById(
            @PathVariable Long providerId) {

        return ApiResponse.<ProviderProfileResponse>builder()
                .success(true)
                .data(service.getProviderById(providerId))
                .build();
    }
}