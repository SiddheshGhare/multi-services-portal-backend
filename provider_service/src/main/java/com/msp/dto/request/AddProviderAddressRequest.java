package com.msp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Getter 
@Setter
@Data
public class AddProviderAddressRequest {

    @NotBlank
    private String addressLine;

    @NotBlank
    private String city;

    @NotBlank
    private String state;

    @NotBlank
    private String pincode;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    private Boolean primaryAddress = true;
}