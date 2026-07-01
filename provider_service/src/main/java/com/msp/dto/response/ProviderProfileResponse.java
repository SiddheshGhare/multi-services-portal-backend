package com.msp.dto.response;

import com.msp.enums.ApprovalStatus;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Getter 
@Setter
@Data
@Builder
public class ProviderProfileResponse {

    private Long id;

    private String fullName;

    private String phone;

    private String profileImage;

    private String description;

    private Integer experienceYears;

    private ApprovalStatus approvalStatus;

    private Double averageRating;

    private Integer totalJobsCompleted;
}