package com.msp.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateAddressRequest {

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

    // optional (if not sent → default true for first address)
    private Boolean primaryAddress;
}