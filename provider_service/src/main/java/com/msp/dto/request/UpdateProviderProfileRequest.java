package com.msp.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Getter 
@Setter
@Data
public class UpdateProviderProfileRequest {

    private String fullName;

    private String phone;

    private String profileImage;

    private String description;

    private Integer experienceYears;
}