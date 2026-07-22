package com.msp.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProviderIdentityResponse {

    private Long providerId;

    private Long authUserId;

    private String providerName;

    private boolean approved;
}