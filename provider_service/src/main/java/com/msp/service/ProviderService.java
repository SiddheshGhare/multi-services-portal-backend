package com.msp.service;

import com.msp.dto.request.CreateProviderProfileRequest;
import com.msp.dto.request.UpdateProviderProfileRequest;
import com.msp.dto.response.ProviderProfileResponse;

import org.springframework.web.multipart.MultipartFile;

public interface ProviderService {

    ProviderProfileResponse createProfile(
            Long authUserId,
            CreateProviderProfileRequest request,
            MultipartFile profileImage
    );

    ProviderProfileResponse getMyProfile(
            Long authUserId
    );

    ProviderProfileResponse updateProfile(
            Long authUserId,
            UpdateProviderProfileRequest request,
            MultipartFile profileImage
    );

    ProviderProfileResponse getProviderById(
            Long providerId
    );
}