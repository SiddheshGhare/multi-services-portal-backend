package com.msp.service;

import com.msp.dto.response.ProviderBookingInfoResponse;
import com.msp.dto.response.ProviderIdentityResponse;

public interface ProviderInternalService {

    ProviderBookingInfoResponse getProviderBookingInfo(
            Long providerId,
            Long categoryId
    );
    ProviderIdentityResponse getProviderIdentityByAuthUserId(
            Long authUserId
    );
}