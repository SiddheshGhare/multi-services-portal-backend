package com.msp.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateServiceCategoryRequest {

    @NotBlank
    private String name;

    private String description;

    private Boolean active;
}