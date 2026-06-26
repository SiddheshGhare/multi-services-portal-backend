package com.msp.entity;

import com.msp.enums.DocumentType;
import com.msp.enums.VerificationStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "provider_document")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProviderDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long providerId;

    @Enumerated(EnumType.STRING)
    private DocumentType documentType;

    private String documentUrl;

    @Enumerated(EnumType.STRING)
    private VerificationStatus verificationStatus = VerificationStatus.PENDING;
}
