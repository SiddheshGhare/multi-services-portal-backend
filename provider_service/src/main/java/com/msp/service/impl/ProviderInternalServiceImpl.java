package com.msp.service.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.msp.dto.response.ProviderBookingInfoResponse;
import com.msp.dto.response.ProviderIdentityResponse;
import com.msp.entity.ProviderProfile;
import com.msp.entity.ProviderSkill;
import com.msp.enums.ApprovalStatus;
import com.msp.exception.ResourceNotFoundException;
import com.msp.repository.ProviderProfileRepository;
import com.msp.repository.ProviderSkillRepository;
import com.msp.service.ProviderInternalService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProviderInternalServiceImpl
        implements ProviderInternalService {

    private final ProviderProfileRepository providerProfileRepository;
    private final ProviderSkillRepository providerSkillRepository;

    @Override
    public ProviderBookingInfoResponse getProviderBookingInfo(
            Long providerId,
            Long categoryId) {

        ProviderProfile provider = providerProfileRepository
                .findById(providerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Provider not found with ID: "
                                        + providerId
                        )
                );

        ProviderSkill skill = providerSkillRepository
                .findByProviderIdAndCategoryId(
                        providerId,
                        categoryId
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Provider does not offer category ID: "
                                        + categoryId
                        )
                );

        if (skill.getCategory() == null) {
            throw new ResourceNotFoundException(
                    "Service category is missing for provider skill"
            );
        }

        if (skill.getBasePrice() == null) {
            throw new IllegalStateException(
                    "Base price is not configured for provider skill"
            );
        }

        return ProviderBookingInfoResponse.builder()
                .providerId(provider.getId())
                .providerName(provider.getFullName())

                .providerApproved(
                        ApprovalStatus.APPROVED.equals(
                                provider.getApprovalStatus()
                        )
                )

                .categoryId(
                        skill.getCategory().getId()
                )

                .categoryName(
                        skill.getCategory().getName()
                )

                .categoryActive(
                        Boolean.TRUE.equals(
                                skill.getCategory().getActive()
                        )
                )

                .basePrice(
                        BigDecimal.valueOf(
                                skill.getBasePrice()
                        )
                )

                .skillActive(
                        Boolean.TRUE.equals(
                                skill.getActive()
                        )
                )

                .build();
    }

    @Override
    public ProviderIdentityResponse getProviderIdentityByAuthUserId(
            Long authUserId) {

        ProviderProfile provider = providerProfileRepository
                .findByAuthUserId(authUserId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Provider profile not found for auth user ID: "
                                        + authUserId
                        )
                );

        return ProviderIdentityResponse.builder()
                .providerId(provider.getId())
                .authUserId(provider.getAuthUserId())
                .providerName(provider.getFullName())

                .approved(
                        ApprovalStatus.APPROVED.equals(
                                provider.getApprovalStatus()
                        )
                )

                .build();
    }
}