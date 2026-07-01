package com.msp.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Getter 
@Setter
@Data
public class AddProviderSkillRequest {

    @NotNull
    private Long serviceCategoryId;

    @NotNull
    private Double basePrice;
}