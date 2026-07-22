package com.msp.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.msp.dto.response.BookingResponse;
import com.msp.service.AdminBookingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/bookings")
@RequiredArgsConstructor
public class AdminBookingController {

    private final AdminBookingService adminBookingService;

    @GetMapping
    public ResponseEntity<List<BookingResponse>> getAllBookings() {

        return ResponseEntity.ok(
                adminBookingService.getAllBookings()
        );
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingResponse> getBookingById(
            @PathVariable Long bookingId) {

        return ResponseEntity.ok(
                adminBookingService.getBookingById(bookingId)
        );
    }
}