package com.msp.service;

import java.util.List;

import com.msp.dto.request.RejectBookingRequest;
import com.msp.dto.response.BookingResponse;

public interface ProviderBookingService {

    List<BookingResponse> getPendingBookings(
            Long providerId
    );

    List<BookingResponse> getProviderBookings(
            Long providerId
    );

    BookingResponse acceptBooking(
            Long bookingId,
            Long providerId
    );

    BookingResponse rejectBooking(
            Long bookingId,
            Long providerId,
            RejectBookingRequest request
    );

    BookingResponse startBooking(
            Long bookingId,
            Long providerId
    );

    BookingResponse completeBooking(
            Long bookingId,
            Long providerId
    );
}