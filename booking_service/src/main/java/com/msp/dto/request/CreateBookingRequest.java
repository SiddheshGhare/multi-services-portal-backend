package com.msp.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBookingRequest {

    @NotNull(message = "Provider ID is required")
    private Long providerId;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    @NotBlank(message = "Service address is required")
    @Size(
        max = 500,
        message = "Service address cannot exceed 500 characters"
    )
    private String serviceAddress;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Pincode is required")
    @Pattern(
        regexp = "^[1-9][0-9]{5}$",
        message = "Invalid pincode"
    )
    private String pincode;

    @NotNull(message = "Scheduled date is required")
    @FutureOrPresent(
        message = "Scheduled date cannot be in the past"
    )
    private LocalDate scheduledDate;

    @NotNull(message = "Scheduled time is required")
    private LocalTime scheduledTime;

    @Size(
        max = 1000,
        message = "Description cannot exceed 1000 characters"
    )
    private String description;
}