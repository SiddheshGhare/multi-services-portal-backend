package com.msp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RejectBookingRequest {

    @NotBlank(message = "Rejection reason is required")
    @Size(
        max = 500,
        message = "Rejection reason cannot exceed 500 characters"
    )
    private String rejectionReason;
}