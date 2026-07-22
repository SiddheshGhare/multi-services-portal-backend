package com.msp.dto.request;

import com.msp.enums.CancellationReason;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CancelBookingRequest {

    @NotNull(message = "Cancellation reason is required")
    private CancellationReason cancellationReason;

    @Size(
        max = 500,
        message = "Cancellation remarks cannot exceed 500 characters"
    )
    private String cancellationRemarks;
}