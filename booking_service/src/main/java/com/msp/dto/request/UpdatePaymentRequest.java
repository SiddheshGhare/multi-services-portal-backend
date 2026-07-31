package com.msp.dto.request;

import com.msp.enums.PaymentMethod;
import com.msp.enums.PaymentStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePaymentRequest {

    @NotNull(message = "Payment status is required")
    private PaymentStatus paymentStatus;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;
}