package com.msp.service.impl;

import com.msp.dto.request.CreateProviderProfileRequest;
import com.msp.dto.request.UpdateProviderProfileRequest;
import com.msp.dto.response.ProviderProfileResponse;
import com.msp.entity.ProviderProfile;
import com.msp.enums.ApprovalStatus;
import com.msp.repository.ProviderProfileRepository;
import com.msp.service.ProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProviderServiceImpl implements ProviderService {

    private final ProviderProfileRepository repository;

    @Override
    public ProviderProfileResponse createProfile(Long authUserId, CreateProviderProfileRequest request) {

        if (repository.existsByAuthUserId(authUserId)) {
            throw new RuntimeException("Provider profile already exists");
        }

        ProviderProfile profile = ProviderProfile.builder()
                .authUserId(authUserId)
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .profileImage(request.getProfileImage())
                .description(request.getDescription())
                .experienceYears(request.getExperienceYears())
                .approvalStatus(ApprovalStatus.PENDING)
                .averageRating(0.0)
                .totalJobsCompleted(0)
                .build();

        repository.save(profile);

        return mapToResponse(profile);
    }

    @Override
    public ProviderProfileResponse getMyProfile(Long authUserId) {

        ProviderProfile profile = repository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        return mapToResponse(profile);
    }

    @Override
    public ProviderProfileResponse updateProfile(Long authUserId, UpdateProviderProfileRequest request) {

        ProviderProfile profile = repository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        if (request.getFullName() != null)
            profile.setFullName(request.getFullName());

        if (request.getPhone() != null)
            profile.setPhone(request.getPhone());

        if (request.getProfileImage() != null)
            profile.setProfileImage(request.getProfileImage());

        if (request.getDescription() != null)
            profile.setDescription(request.getDescription());

        if (request.getExperienceYears() != null)
            profile.setExperienceYears(request.getExperienceYears());

        repository.save(profile);

        return mapToResponse(profile);
    }

    @Override
    public ProviderProfileResponse getProviderById(Long providerId) {

        ProviderProfile profile = repository.findById(providerId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        return mapToResponse(profile);
    }

    private ProviderProfileResponse mapToResponse(ProviderProfile p) {

        return ProviderProfileResponse.builder()
                .id(p.getId())
                .fullName(p.getFullName())
                .phone(p.getPhone())
                .profileImage(p.getProfileImage())
                .description(p.getDescription())
                .experienceYears(p.getExperienceYears())
                .approvalStatus(p.getApprovalStatus())
                .averageRating(p.getAverageRating())
                .totalJobsCompleted(p.getTotalJobsCompleted())
                .build();
    }
}