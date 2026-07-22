package com.msp.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.msp.dto.request.RejectBookingRequest;
import com.msp.dto.response.BookingResponse;
import com.msp.service.ProviderBookingService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/provider/bookings")
@RequiredArgsConstructor
public class ProviderBookingController {

    private final ProviderBookingService providerBookingService;

    @GetMapping("/pending")
    public ResponseEntity<List<BookingResponse>> getPendingBookings(
            @RequestHeader("X-User-Id") Long authUserId) {

        return ResponseEntity.ok(
                providerBookingService.getPendingBookings(authUserId)
        );
    }

    @GetMapping
    public ResponseEntity<List<BookingResponse>> getProviderBookings(
            @RequestHeader("X-User-Id") Long authUserId) {

        return ResponseEntity.ok(
                providerBookingService.getProviderBookings(authUserId)
        );
    }

    @PutMapping("/{bookingId}/accept")
    public ResponseEntity<BookingResponse> acceptBooking(
            @PathVariable Long bookingId,
            @RequestHeader("X-User-Id") Long authUserId) {

        return ResponseEntity.ok(
                providerBookingService.acceptBooking(
                        bookingId,
                        authUserId
                )
        );
    }

    @PutMapping("/{bookingId}/reject")
    public ResponseEntity<BookingResponse> rejectBooking(
            @PathVariable Long bookingId,
            @RequestHeader("X-User-Id") Long authUserId,
            @Valid @RequestBody RejectBookingRequest request) {

        return ResponseEntity.ok(
                providerBookingService.rejectBooking(
                        bookingId,
                        authUserId,
                        request
                )
        );
    }

    @PutMapping("/{bookingId}/start")
    public ResponseEntity<BookingResponse> startBooking(
            @PathVariable Long bookingId,
            @RequestHeader("X-User-Id") Long authUserId) {

        return ResponseEntity.ok(
                providerBookingService.startBooking(
                        bookingId,
                        authUserId
                )
        );
    }

    @PutMapping("/{bookingId}/complete")
    public ResponseEntity<BookingResponse> completeBooking(
            @PathVariable Long bookingId,
            @RequestHeader("X-User-Id") Long authUserId) {

        return ResponseEntity.ok(
                providerBookingService.completeBooking(
                        bookingId,
                        authUserId
                )
        );
    }
}