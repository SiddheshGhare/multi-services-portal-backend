package com.msp.service;

import com.msp.dto.response.ProviderBookingInfoResponse;
import com.msp.entity.Booking;

public interface BookingNotificationDispatcher {

    void sendBookingCreated(Booking booking, ProviderBookingInfoResponse providerInfo);

    void sendBookingAccepted(Booking booking);

    void sendBookingRejected(Booking booking);

    void sendBookingCancelled(Booking booking);
}
