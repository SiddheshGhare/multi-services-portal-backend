package com.msp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Getter 
@Setter
@Data
public class CreateProviderProfileRequest {

    @NotBlank
    private String fullName;

    @NotBlank
    private String phone;

    private String profileImage;

    private String description;

    @NotNull
    private Integer experienceYears;
}