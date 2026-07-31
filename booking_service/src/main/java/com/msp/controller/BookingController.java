package com.msp.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.msp.dto.request.UpdatePaymentRequest;

import com.msp.dto.request.CancelBookingRequest;
import com.msp.dto.request.CreateBookingRequest;
import com.msp.dto.response.BookingResponse;
import com.msp.service.BookingService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(
            @RequestHeader("X-User-Id") Long customerId,
            @Valid @RequestBody CreateBookingRequest request) {

        BookingResponse response =
                bookingService.createBooking(customerId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/my")
    public ResponseEntity<List<BookingResponse>> getMyBookings(
            @RequestHeader("X-User-Id") Long customerId) {

        return ResponseEntity.ok(
                bookingService.getMyBookings(customerId)
        );
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingResponse> getBookingById(
            @PathVariable Long bookingId,
            @RequestHeader("X-User-Id") Long customerId) {

        return ResponseEntity.ok(
                bookingService.getBookingById(
                        bookingId,
                        customerId
                )
        );
    }
    
    @PutMapping("/{bookingId}/payment")
    public ResponseEntity<BookingResponse> updatePaymentStatus(
            @PathVariable Long bookingId,
            @RequestHeader("X-User-Id") Long customerId,
            @Valid @RequestBody UpdatePaymentRequest request) {

        return ResponseEntity.ok(
                bookingService.updatePaymentStatus(
                        bookingId,
                        customerId,
                        request
                )
        );
    }

    @PutMapping("/{bookingId}/cancel")
    public ResponseEntity<BookingResponse> cancelBooking(
            @PathVariable Long bookingId,
            @RequestHeader("X-User-Id") Long customerId,
            @Valid @RequestBody CancelBookingRequest request) {

        return ResponseEntity.ok(
                bookingService.cancelBooking(
                        bookingId,
                        customerId,
                        request
                )
        );
    }
}