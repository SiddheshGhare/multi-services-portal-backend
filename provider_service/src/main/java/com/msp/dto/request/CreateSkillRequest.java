package com.msp.dto.request;



import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateSkillRequest {

    @NotNull
    private Long categoryId;

    @NotNull
    private Double basePrice;
}