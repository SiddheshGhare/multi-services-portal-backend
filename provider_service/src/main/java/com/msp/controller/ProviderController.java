package com.msp.controller;

import com.msp.dto.common.ApiResponse;
import com.msp.dto.request.CreateProviderProfileRequest;
import com.msp.dto.request.UpdateProviderProfileRequest;
import com.msp.dto.response.ProviderProfileResponse;
import com.msp.service.ProviderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/providers")
@RequiredArgsConstructor
public class ProviderController {

    private final ProviderService service;

    @PostMapping(
            value = "/profile/createProfile",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ApiResponse<ProviderProfileResponse> createProfile(
            @RequestHeader("X-User-Id") Long authUserId,

            @Valid
            @RequestPart("profile")
            CreateProviderProfileRequest request,

            @RequestPart(
                    value = "profileImage",
                    required = false
            )
            MultipartFile profileImage) {

        return ApiResponse.<ProviderProfileResponse>builder()
                .success(true)
                .message("Profile created successfully")
                .data(
                        service.createProfile(
                                authUserId,
                                request,
                                profileImage
                        )
                )
                .build();
    }

    @GetMapping("/profile/getProfile")
    public ApiResponse<ProviderProfileResponse> getMyProfile(
            @RequestHeader("X-User-Id") Long authUserId) {

        return ApiResponse.<ProviderProfileResponse>builder()
                .success(true)
                .message("Profile fetched successfully")
                .data(service.getMyProfile(authUserId))
                .build();
    }

    @PutMapping(
            value = "/profile/updateProfile",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ApiResponse<ProviderProfileResponse> updateProfile(
            @RequestHeader("X-User-Id") Long authUserId,

            @Valid
            @RequestPart("profile")
            UpdateProviderProfileRequest request,

            @RequestPart(
                    value = "profileImage",
                    required = false
            )
            MultipartFile profileImage) {

        return ApiResponse.<ProviderProfileResponse>builder()
                .success(true)
                .message("Profile updated successfully")
                .data(
                        service.updateProfile(
                                authUserId,
                                request,
                                profileImage
                        )
                )
                .build();
    }

    @GetMapping("/getProviderById/{providerId}")
    public ApiResponse<ProviderProfileResponse> getProviderById(
            @PathVariable Long providerId) {

        return ApiResponse.<ProviderProfileResponse>builder()
                .success(true)
                .message("Provider fetched successfully")
                .data(service.getProviderById(providerId))
                .build();
    }
}