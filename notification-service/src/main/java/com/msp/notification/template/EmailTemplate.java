package com.msp.notification.template;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmailTemplate {
    EMAIL_VERIFICATION("generic-notification"),
    PASSWORD_RESET("generic-notification"),
    WELCOME_USER("generic-notification"),
    PROVIDER_APPROVED("provider-approved"),
    PROVIDER_REJECTED("provider-rejected"),
    DOCUMENT_APPROVED("document-approved"),
    DOCUMENT_REJECTED("document-rejected"),
    BOOKING_CREATED("booking-created"),
    BOOKING_ACCEPTED("booking-accepted"),
    BOOKING_REJECTED("booking-rejected"),
    BOOKING_ASSIGNED("generic-notification"),
    BOOKING_CANCELLED("booking-cancelled"),
    BOOKING_RESCHEDULED("generic-notification"),
    NEW_BOOKING_REQUEST("new-booking-request");

    private final String templateName;
}
