package com.msp.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Getter 
@Setter
@Data
public class UpdateProviderSkillRequest {

    private Double basePrice;

    private Boolean active;
}