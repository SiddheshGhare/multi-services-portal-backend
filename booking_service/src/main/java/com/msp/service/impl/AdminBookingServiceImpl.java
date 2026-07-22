package com.msp.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.msp.dto.response.BookingResponse;
import com.msp.entity.Booking;
import com.msp.exception.ResourceNotFoundException;
import com.msp.repository.BookingRepository;
import com.msp.service.AdminBookingService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminBookingServiceImpl implements AdminBookingService {

    private final BookingRepository bookingRepository;

    @Override
    public List<BookingResponse> getAllBookings() {

        return bookingRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public BookingResponse getBookingById(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Booking not found with ID: " + bookingId
                ));

        return mapToResponse(booking);
    }

    private BookingResponse mapToResponse(Booking booking) {

        return BookingResponse.builder()
                .id(booking.getId())
                .bookingNumber(booking.getBookingNumber())
                .customerId(booking.getCustomerId())
                .providerId(booking.getProviderId())
                .categoryId(booking.getCategoryId())
                .serviceAddress(booking.getServiceAddress())
                .city(booking.getCity())
                .state(booking.getState())
                .pincode(booking.getPincode())
                .scheduledDate(booking.getScheduledDate())
                .scheduledTime(booking.getScheduledTime())
                .description(booking.getDescription())
                .estimatedPrice(booking.getEstimatedPrice())
                .finalPrice(booking.getFinalPrice())
                .status(booking.getStatus())
                .paymentStatus(booking.getPaymentStatus())
                .cancellationReason(
                        booking.getCancellationReason()
                )
                .cancellationRemarks(
                        booking.getCancellationRemarks()
                )
                .rejectionReason(
                        booking.getRejectionReason()
                )
                .createdAt(booking.getCreatedAt())
                .updatedAt(booking.getUpdatedAt())
                .build();
    }
}