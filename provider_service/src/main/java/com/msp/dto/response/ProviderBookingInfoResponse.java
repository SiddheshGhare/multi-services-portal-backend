package com.msp.dto.response;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProviderBookingInfoResponse {

    private Long providerId;

    private Long providerAuthUserId;

    private Long categoryId;

    private String providerName;

    private String categoryName;

    private BigDecimal basePrice;

    private boolean providerApproved;

    private boolean skillActive;

    private boolean categoryActive;
}
