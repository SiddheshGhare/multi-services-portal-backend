package com.msp.service;

import java.util.List;

import com.msp.dto.response.BookingResponse;

public interface AdminBookingService {

    List<BookingResponse> getAllBookings();

    BookingResponse getBookingById(Long bookingId);
}