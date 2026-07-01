package com.msp.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Getter 
@Setter
@Data
public class CreateCategoryRequest {

    @NotBlank
    private String name;

    private String description;
}
