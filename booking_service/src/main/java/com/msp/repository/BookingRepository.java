package com.msp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.msp.entity.Booking;
import com.msp.enums.BookingStatus;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByCustomerIdOrderByCreatedAtDesc(Long customerId);

    List<Booking> findByProviderIdOrderByCreatedAtDesc(Long providerId);

    List<Booking> findByProviderIdAndStatusOrderByCreatedAtDesc(
            Long providerId,
            BookingStatus status
    );

    Optional<Booking> findByIdAndCustomerId(
            Long id,
            Long customerId
    );

    Optional<Booking> findByIdAndProviderId(
            Long id,
            Long providerId
    );

    boolean existsByBookingNumber(String bookingNumber);
}