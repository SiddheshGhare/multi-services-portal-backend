package com.msp.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Getter 
@Setter
@Data
@Builder
public class ProviderSkillResponse {

    private Long id;

    private Long serviceCategoryId;

    private String serviceCategoryName;

    private Double basePrice;

    private Boolean active;
}