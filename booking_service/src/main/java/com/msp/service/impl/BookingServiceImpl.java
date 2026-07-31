package com.msp.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.msp.dto.request.CancelBookingRequest;
import com.msp.dto.request.CreateBookingRequest;
import com.msp.dto.request.UpdatePaymentRequest;
import com.msp.dto.response.BookingResponse;
import com.msp.dto.response.ProviderBookingInfoResponse;
import com.msp.entity.Booking;
import com.msp.enums.BookingStatus;
import com.msp.enums.PaymentStatus;
import com.msp.exception.BadRequestException;
import com.msp.exception.ResourceNotFoundException;
import com.msp.feign.ProviderClient;
import com.msp.repository.BookingRepository;
import com.msp.service.BookingNotificationDispatcher;
import com.msp.service.BookingService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ProviderClient providerClient;
    private final BookingNotificationDispatcher bookingNotificationDispatcher;

    @Override
    public BookingResponse createBooking(
            Long customerId,
            CreateBookingRequest request) {

        ProviderBookingInfoResponse providerInfo =
                providerClient.getProviderBookingInfo(
                        request.getProviderId(),
                        request.getCategoryId()
                );

        if (!providerInfo.isProviderApproved()) {
            throw new BadRequestException(
                    "Selected provider is not approved"
            );
        }

        if (!providerInfo.isSkillActive()) {
            throw new BadRequestException(
                    "Selected provider service is inactive"
            );
        }

        if (!providerInfo.isCategoryActive()) {
            throw new BadRequestException(
                    "Selected service category is inactive"
            );
        }

        Booking booking = Booking.builder()
                .customerId(customerId)
                .providerId(providerInfo.getProviderId())
                .categoryId(providerInfo.getCategoryId())
                .serviceAddress(request.getServiceAddress())
                .city(request.getCity())
                .state(request.getState())
                .pincode(request.getPincode())
                .scheduledDate(request.getScheduledDate())
                .scheduledTime(request.getScheduledTime())
                .description(request.getDescription())
                .estimatedPrice(providerInfo.getBasePrice())
                .build();

        Booking savedBooking = bookingRepository.save(booking);
        bookingNotificationDispatcher.sendBookingCreated(savedBooking, providerInfo);

        return mapToResponse(savedBooking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getMyBookings(Long customerId) {

        return bookingRepository
                .findByCustomerIdOrderByCreatedAtDesc(customerId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponse getBookingById(
            Long bookingId,
            Long customerId) {

        Booking booking = bookingRepository
                .findByIdAndCustomerId(bookingId, customerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Booking not found with ID: " + bookingId
                ));

        return mapToResponse(booking);
    }

    @Override
    public BookingResponse cancelBooking(
            Long bookingId,
            Long customerId,
            CancelBookingRequest request) {

        Booking booking = bookingRepository
                .findByIdAndCustomerId(bookingId, customerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Booking not found with ID: " + bookingId
                ));

        if (booking.getStatus() == BookingStatus.COMPLETED) {
            throw new BadRequestException(
                    "Completed booking cannot be cancelled"
            );
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BadRequestException(
                    "Booking is already cancelled"
            );
        }

        if (booking.getStatus() == BookingStatus.REJECTED) {
            throw new BadRequestException(
                    "Rejected booking cannot be cancelled"
            );
        }

        if (booking.getStatus() == BookingStatus.IN_PROGRESS) {
            throw new BadRequestException(
                    "Booking cannot be cancelled after service has started"
            );
        }

        booking.setStatus(BookingStatus.CANCELLED);
        booking.setCancellationReason(
                request.getCancellationReason()
        );
        booking.setCancellationRemarks(
                request.getCancellationRemarks()
        );

        Booking updatedBooking = bookingRepository.save(booking);
        bookingNotificationDispatcher.sendBookingCancelled(updatedBooking);

        return mapToResponse(updatedBooking);
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
                .paymentMethod(booking.getPaymentMethod())
                .cancellationReason(
                        booking.getCancellationReason()
                )
                .cancellationRemarks(
                        booking.getCancellationRemarks()
                )
                .rejectionReason(booking.getRejectionReason())
                .createdAt(booking.getCreatedAt())
                .updatedAt(booking.getUpdatedAt())
                .build();
    }
    
    @Override
    @Transactional
    public BookingResponse updatePaymentStatus(
            Long bookingId,
            Long customerId,
            UpdatePaymentRequest request) {

        Booking booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Booking not found with ID: "
                                        + bookingId
                        )
                );

        if (!booking.getCustomerId().equals(customerId)) {
            throw new BadRequestException(
                    "You are not allowed to pay for this booking"
            );
        }

        if (booking.getStatus() != BookingStatus.COMPLETED) {
            throw new BadRequestException(
                    "Payment is allowed only after booking completion"
            );
        }

        if (booking.getPaymentStatus() == PaymentStatus.PAID) {
            throw new BadRequestException(
                    "Payment has already been completed"
            );
        }

        if (request.getPaymentStatus() != PaymentStatus.PAID) {
            throw new BadRequestException(
                    "Payment status must be PAID"
            );
        }

        booking.setPaymentStatus(
                PaymentStatus.PAID
        );

        booking.setPaymentMethod(
                request.getPaymentMethod()
        );

        Booking savedBooking =
                bookingRepository.save(booking);

        return mapToResponse(savedBooking);
    }
}
