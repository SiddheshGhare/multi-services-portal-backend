package com.msp.service.impl;

import com.msp.dto.request.EmailNotificationRequest;
import com.msp.dto.response.AuthUserSummaryResponse;
import com.msp.dto.response.NotificationResponse;
import com.msp.dto.response.ProviderBookingInfoResponse;
import com.msp.entity.Booking;
import com.msp.feign.AuthServiceClient;
import com.msp.feign.NotificationServiceClient;
import com.msp.feign.ProviderClient;
import com.msp.service.BookingNotificationDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingNotificationDispatcherImpl implements BookingNotificationDispatcher {

    private final NotificationServiceClient notificationServiceClient;
    private final AuthServiceClient authServiceClient;
    private final ProviderClient providerClient;

    @Override
    public void sendBookingCreated(Booking booking, ProviderBookingInfoResponse providerInfo) {
        Map<String, Object> data = bookingData(booking, providerInfo);
        sendToAuthUser(booking.getCustomerId(), "Booking Request Created", "BOOKING_CREATED", data);
        sendToAuthUser(providerInfo.getProviderAuthUserId(), "New Booking Request", "NEW_BOOKING_REQUEST", data);
    }

    @Override
    public void sendBookingAccepted(Booking booking) {
        ProviderBookingInfoResponse providerInfo = getProviderInfo(booking);
        sendToAuthUser(booking.getCustomerId(), "Booking Accepted", "BOOKING_ACCEPTED", bookingData(booking, providerInfo));
    }

    @Override
    public void sendBookingRejected(Booking booking) {
        ProviderBookingInfoResponse providerInfo = getProviderInfo(booking);
        Map<String, Object> data = bookingData(booking, providerInfo);
        data.put("rejectionReason", booking.getRejectionReason());
        sendToAuthUser(booking.getCustomerId(), "Booking Rejected", "BOOKING_REJECTED", data);
    }

    @Override
    public void sendBookingCancelled(Booking booking) {
        ProviderBookingInfoResponse providerInfo = getProviderInfo(booking);
        Map<String, Object> data = bookingData(booking, providerInfo);
        data.put("cancellationReason", booking.getCancellationReason());
        data.put("cancellationRemarks", booking.getCancellationRemarks());

        sendToAuthUser(booking.getCustomerId(), "Booking Cancelled", "BOOKING_CANCELLED", data);
        sendToAuthUser(providerInfo.getProviderAuthUserId(), "Booking Cancelled by Customer", "BOOKING_CANCELLED", data);
    }

    private ProviderBookingInfoResponse getProviderInfo(Booking booking) {
        return providerClient.getProviderBookingInfo(booking.getProviderId(), booking.getCategoryId());
    }

    private Map<String, Object> bookingData(Booking booking, ProviderBookingInfoResponse providerInfo) {
        Map<String, Object> data = new HashMap<>();
        data.put("bookingNumber", booking.getBookingNumber());
        data.put("providerName", providerInfo.getProviderName());
        data.put("categoryName", providerInfo.getCategoryName());
        data.put("city", booking.getCity());
        data.put("scheduledDate", booking.getScheduledDate());
        data.put("scheduledTime", booking.getScheduledTime());
        return data;
    }

    private void sendToAuthUser(Long authUserId, String subject, String template, Map<String, Object> data) {
        try {
            AuthUserSummaryResponse user = authServiceClient.getUserById(authUserId);
            NotificationResponse response = notificationServiceClient.sendEmail(
                    EmailNotificationRequest.builder()
                            .to(user.getEmail())
                            .subject(subject)
                            .template(template)
                            .data(data)
                            .build()
            );

            if (!response.isSuccess()) {
                log.warn("Notification service returned failure for template {} and user {}: {}", template, authUserId, response.getMessage());
            }
        } catch (Exception ex) {
            log.warn("Failed to send booking notification for template {} and user {}", template, authUserId, ex);
        }
    }
}
