package com.msp.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import com.msp.enums.BookingStatus;
import com.msp.enums.CancellationReason;
import com.msp.enums.PaymentStatus;
import com.msp.enums.PaymentMethod;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
        name = "booking_number",
        nullable = false,
        unique = true,
        updatable = false
    )
    private String bookingNumber;

    /*
     * authUserId received from API Gateway.
     * We store the authentication user ID, not a UserProfile entity.
     */
    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    /*
     * ProviderProfile ID selected by the customer.
     */
    @Column(name = "provider_id", nullable = false)
    private Long providerId;

    /*
     * ServiceCategory ID from Provider Service.
     */
    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(name = "service_address", nullable = false, length = 500)
    private String serviceAddress;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false, length = 10)
    private String pincode;

    @Column(name = "scheduled_date", nullable = false)
    private LocalDate scheduledDate;

    @Column(name = "scheduled_time", nullable = false)
    private LocalTime scheduledTime;

    @Column(length = 1000)
    private String description;

    @Column(name = "estimated_price", precision = 10, scale = 2)
    private BigDecimal estimatedPrice;

    @Column(name = "final_price", precision = 10, scale = 2)
    private BigDecimal finalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "cancellation_reason")
    private CancellationReason cancellationReason;

    @Column(name = "cancellation_remarks", length = 500)
    private String cancellationRemarks;

    @Column(name = "rejection_reason", length = 500)
    private String rejectionReason;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {

        LocalDateTime now = LocalDateTime.now();

        this.createdAt = now;
        this.updatedAt = now;

        if (this.bookingNumber == null) {
            this.bookingNumber =
                    "BK-" + UUID.randomUUID()
                            .toString()
                            .substring(0, 8)
                            .toUpperCase();
        }

        if (this.status == null) {
            this.status = BookingStatus.PENDING;
        }

        if (this.paymentStatus == null) {
            this.paymentStatus = PaymentStatus.PENDING;
        }
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}