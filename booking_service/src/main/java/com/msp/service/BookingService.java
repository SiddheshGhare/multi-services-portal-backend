package com.msp.service;

import java.util.List;

import com.msp.dto.request.CancelBookingRequest;
import com.msp.dto.request.CreateBookingRequest;
import com.msp.dto.response.BookingResponse;

public interface BookingService {

    BookingResponse createBooking(
            Long customerId,
            CreateBookingRequest request
    );

    List<BookingResponse> getMyBookings(
            Long customerId
    );

    BookingResponse getBookingById(
            Long bookingId,
            Long customerId
    );

    BookingResponse cancelBooking(
            Long bookingId,
            Long customerId,
            CancelBookingRequest request
    );
}