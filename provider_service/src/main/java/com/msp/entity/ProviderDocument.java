package com.msp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.msp.enums.DocumentType;
import com.msp.enums.VerificationStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "provider_document",
        indexes = {
                @Index(name = "idx_provider_document", columnList = "provider_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProviderDocument extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    @JsonIgnore
    private ProviderProfile provider;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentType documentType;

    @Column(nullable = false)
    private String documentUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerificationStatus verificationStatus = VerificationStatus.PENDING;

    @Column(length = 500)
    private String remarks;
}