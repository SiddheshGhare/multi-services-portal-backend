package com.msp.dto.response;

import com.msp.enums.ApprovalStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
@Getter 
@Setter
@Data
@Builder
public class ProviderDetailsResponse {

    private Long id;

    private String fullName;

    private String phone;

    private String profileImage;

    private String description;

    private Integer experienceYears;

    private ApprovalStatus approvalStatus;

    private Double averageRating;

    private Integer totalJobsCompleted;

    private List<ProviderSkillResponse> skills;

    private List<ProviderAddressResponse> addresses;

    private List<ProviderDocumentResponse> documents;
}