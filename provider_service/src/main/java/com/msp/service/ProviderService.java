package com.msp.service;

import com.msp.dto.request.CreateProviderProfileRequest;
import com.msp.dto.request.UpdateProviderProfileRequest;
import com.msp.dto.response.ProviderProfileResponse;

public interface ProviderService {

    ProviderProfileResponse createProfile(Long authUserId, CreateProviderProfileRequest request);

    ProviderProfileResponse getMyProfile(Long authUserId);

    ProviderProfileResponse updateProfile(Long authUserId, UpdateProviderProfileRequest request);

    ProviderProfileResponse getProviderById(Long providerId);
}