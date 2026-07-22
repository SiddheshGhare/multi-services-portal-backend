package com.msp.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProviderIdentityResponse {

    private Long providerId;

    private Long authUserId;

    private String providerName;

    private boolean approved;
}