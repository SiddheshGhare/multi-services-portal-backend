package com.msp.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Getter 
@Setter
@Data
@Builder
public class ServiceCategoryResponse {

    private Long id;

    private String name;

    private String description;

    private Boolean active;
}