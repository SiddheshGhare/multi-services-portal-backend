package com.msp.service.impl;

import com.msp.dto.request.CreateProviderProfileRequest;
import com.msp.dto.request.UpdateProviderProfileRequest;
import com.msp.dto.response.ProviderProfileResponse;
import com.msp.entity.ProviderProfile;
import com.msp.enums.ApprovalStatus;
import com.msp.exception.BadRequestException;
import com.msp.exception.ResourceNotFoundException;
import com.msp.repository.ProviderProfileRepository;
import com.msp.service.CloudinaryService;
import com.msp.service.ProviderService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class ProviderServiceImpl implements ProviderService {

    private final ProviderProfileRepository providerProfileRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public ProviderProfileResponse createProfile(
            Long authUserId,
            CreateProviderProfileRequest request,
            MultipartFile profileImage) {

        if (providerProfileRepository.existsByAuthUserId(authUserId)) {
            throw new BadRequestException(
                    "Provider profile already exists"
            );
        }

        String profileImageUrl = null;

        if (profileImage != null && !profileImage.isEmpty()) {
            profileImageUrl =
                    cloudinaryService.uploadFile(profileImage);
        }

        ProviderProfile provider = new ProviderProfile();

        provider.setAuthUserId(authUserId);
        provider.setFullName(request.getFullName());
        provider.setPhone(request.getPhone());
        provider.setDescription(request.getDescription());
        provider.setExperienceYears(
                request.getExperienceYears()
        );
        provider.setProfileImage(profileImageUrl);
        provider.setApprovalStatus(ApprovalStatus.PENDING);
        provider.setAverageRating(0.0);
        provider.setTotalJobsCompleted(0);

        ProviderProfile savedProvider =
                providerProfileRepository.save(provider);

        return mapToResponse(savedProvider);
    }

    @Override
    @Transactional(readOnly = true)
    public ProviderProfileResponse getMyProfile(
            Long authUserId) {

        ProviderProfile provider =
                findByAuthUserId(authUserId);

        return mapToResponse(provider);
    }

    @Override
    public ProviderProfileResponse updateProfile(
            Long authUserId,
            UpdateProviderProfileRequest request,
            MultipartFile profileImage) {

        ProviderProfile provider =
                findByAuthUserId(authUserId);

        provider.setFullName(request.getFullName());
        provider.setPhone(request.getPhone());
        provider.setDescription(request.getDescription());
        provider.setExperienceYears(
                request.getExperienceYears()
        );

        if (profileImage != null && !profileImage.isEmpty()) {

            String newImageUrl =
                    cloudinaryService.uploadFile(profileImage);

            provider.setProfileImage(newImageUrl);
        }

        ProviderProfile updatedProvider =
                providerProfileRepository.save(provider);

        return mapToResponse(updatedProvider);
    }
    @Override
    @Transactional(readOnly = true)
    public ProviderProfileResponse getProviderById(
            Long providerId) {

        ProviderProfile provider =
                providerProfileRepository
                        .findById(providerId)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Provider not found with id: "
                                                + providerId
                                )
                        );

        return mapToResponse(provider);
    }

    private ProviderProfile findByAuthUserId(
            Long authUserId) {

        return providerProfileRepository
                .findByAuthUserId(authUserId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Provider profile not found"
                        )
                );
    }

    private ProviderProfileResponse mapToResponse(
            ProviderProfile provider) {

        return ProviderProfileResponse.builder()
        		.providerId(provider.getId())
                .authUserId(provider.getAuthUserId())
                .fullName(provider.getFullName())
                .phone(provider.getPhone())
                .profileImage(provider.getProfileImage())
                .description(provider.getDescription())
                .experienceYears(
                        provider.getExperienceYears()
                )
                .approvalStatus(
                        provider.getApprovalStatus()
                )
                .averageRating(
                        provider.getAverageRating()
                )
                .totalJobsCompleted(
                        provider.getTotalJobsCompleted()
                )
                .build();
    }
}