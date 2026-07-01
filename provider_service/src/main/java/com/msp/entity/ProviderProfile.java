package com.msp.entity;

import com.msp.enums.ApprovalStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "provider_profile",
        indexes = {
                @Index(name = "idx_auth_user", columnList = "authUserId"),
                @Index(name = "idx_approval", columnList = "approvalStatus")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProviderProfile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column(nullable = false, unique = true)
    private Long authUserId;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String phone;

    private String profileImage;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private Integer experienceYears = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING;

    @Column(nullable = false)
    private Double averageRating = 0.0;

    @Column(nullable = false)
    private Integer totalJobsCompleted = 0;
}