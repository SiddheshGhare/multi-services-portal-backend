package com.msp.dto.response;

import com.msp.enums.ApprovalStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProviderProfileResponse {

    private Long providerId;

    private Long authUserId;

    private String fullName;

    private String phone;

    private String profileImage;

    private String description;

    private Integer experienceYears;

    private ApprovalStatus approvalStatus;

    private Double averageRating;

    private Integer totalJobsCompleted;
}