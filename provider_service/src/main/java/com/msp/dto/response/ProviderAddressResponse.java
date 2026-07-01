package com.msp.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Getter 
@Setter
@Data
@Builder
public class ProviderAddressResponse {

    private Long id;

    private String addressLine;

    private String city;

    private String state;

    private String pincode;

    private Double latitude;

    private Double longitude;

    private Boolean primaryAddress;
}