package com.msp.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Getter 
@Setter
@Data
public class UpdateCategoryRequest {

    private String name;

    private String description;

    private Boolean active;
}