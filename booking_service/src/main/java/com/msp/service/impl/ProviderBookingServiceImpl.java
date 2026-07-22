package com.msp.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.msp.dto.request.RejectBookingRequest;
import com.msp.dto.response.BookingResponse;
import com.msp.dto.response.ProviderIdentityResponse;
import com.msp.entity.Booking;
import com.msp.enums.BookingStatus;
import com.msp.exception.BadRequestException;
import com.msp.exception.ResourceNotFoundException;
import com.msp.feign.ProviderClient;
import com.msp.repository.BookingRepository;
import com.msp.service.ProviderBookingService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProviderBookingServiceImpl
        implements ProviderBookingService {

    private final BookingRepository bookingRepository;
    private final ProviderClient providerClient;

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getPendingBookings(
            Long authUserId) {

        Long providerId = getProviderId(authUserId);

        return bookingRepository
                .findByProviderIdAndStatusOrderByCreatedAtDesc(
                        providerId,
                        BookingStatus.PENDING
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getProviderBookings(
            Long authUserId) {

        Long providerId = getProviderId(authUserId);

        return bookingRepository
                .findByProviderIdOrderByCreatedAtDesc(providerId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public BookingResponse acceptBooking(
            Long bookingId,
            Long authUserId) {

        Long providerId = getProviderId(authUserId);

        Booking booking = getProviderBooking(
                bookingId,
                providerId
        );

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new BadRequestException(
                    "Only pending bookings can be accepted"
            );
        }

        booking.setStatus(BookingStatus.ACCEPTED);

        return mapToResponse(
                bookingRepository.save(booking)
        );
    }

    @Override
    public BookingResponse rejectBooking(
            Long bookingId,
            Long authUserId,
            RejectBookingRequest request) {

        Long providerId = getProviderId(authUserId);

        Booking booking = getProviderBooking(
                bookingId,
                providerId
        );

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new BadRequestException(
                    "Only pending bookings can be rejected"
            );
        }

        booking.setStatus(BookingStatus.REJECTED);
        booking.setRejectionReason(
                request.getRejectionReason()
        );

        return mapToResponse(
                bookingRepository.save(booking)
        );
    }

    @Override
    public BookingResponse startBooking(
            Long bookingId,
            Long authUserId) {

        Long providerId = getProviderId(authUserId);

        Booking booking = getProviderBooking(
                bookingId,
                providerId
        );

        if (booking.getStatus() != BookingStatus.ACCEPTED) {
            throw new BadRequestException(
                    "Only accepted bookings can be started"
            );
        }

        booking.setStatus(BookingStatus.IN_PROGRESS);

        return mapToResponse(
                bookingRepository.save(booking)
        );
    }

    @Override
    public BookingResponse completeBooking(
            Long bookingId,
            Long authUserId) {

        Long providerId = getProviderId(authUserId);

        Booking booking = getProviderBooking(
                bookingId,
                providerId
        );

        if (booking.getStatus() != BookingStatus.IN_PROGRESS) {
            throw new BadRequestException(
                    "Only in-progress bookings can be completed"
            );
        }

        booking.setStatus(BookingStatus.COMPLETED);

        if (booking.getFinalPrice() == null) {
            booking.setFinalPrice(
                    booking.getEstimatedPrice()
            );
        }

        return mapToResponse(
                bookingRepository.save(booking)
        );
    }

    private Long getProviderId(Long authUserId) {

        ProviderIdentityResponse provider =
                providerClient
                        .getProviderIdentityByAuthUserId(
                                authUserId
                        );

        if (!provider.isApproved()) {
            throw new BadRequestException(
                    "Provider account is not approved"
            );
        }

        return provider.getProviderId();
    }

    private Booking getProviderBooking(
            Long bookingId,
            Long providerId) {

        return bookingRepository
                .findByIdAndProviderId(
                        bookingId,
                        providerId
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Booking not found with ID: "
                                        + bookingId
                        )
                );
    }

    private BookingResponse mapToResponse(
            Booking booking) {

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