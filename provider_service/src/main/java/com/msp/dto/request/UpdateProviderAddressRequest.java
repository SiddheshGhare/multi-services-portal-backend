package com.msp.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Getter 
@Setter
@Data
public class UpdateProviderAddressRequest {

    private String addressLine;

    private String city;

    private String state;

    private String pincode;

    private Double latitude;

    private Double longitude;

    private Boolean primaryAddress;
}