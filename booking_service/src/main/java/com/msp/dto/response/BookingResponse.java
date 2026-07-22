package com.msp.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.msp.enums.BookingStatus;
import com.msp.enums.CancellationReason;
import com.msp.enums.PaymentStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookingResponse {

    private Long id;

    private String bookingNumber;

    private Long customerId;

    private Long providerId;

    private Long categoryId;

    private String serviceAddress;

    private String city;

    private String state;

    private String pincode;

    private LocalDate scheduledDate;

    private LocalTime scheduledTime;

    private String description;

    private BigDecimal estimatedPrice;

    private BigDecimal finalPrice;

    private BookingStatus status;

    private PaymentStatus paymentStatus;

    private CancellationReason cancellationReason;

    private String cancellationRemarks;

    private String rejectionReason;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}