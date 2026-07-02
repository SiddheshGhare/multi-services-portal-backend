package com.msp.repository;

import com.msp.entity.ProviderDocument;
import com.msp.enums.DocumentType;
import com.msp.enums.VerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProviderDocumentRepository extends JpaRepository<ProviderDocument, Long> {

    List<ProviderDocument> findByProviderId(Long providerId);

    Optional<ProviderDocument> findByProviderIdAndDocumentType(Long providerId, DocumentType documentType);

    boolean existsByProviderIdAndDocumentType(Long providerId, DocumentType documentType);

    List<ProviderDocument> findByVerificationStatus(VerificationStatus verificationStatus);

    boolean existsByProviderIdAndVerificationStatusNot(
            Long providerId,
            VerificationStatus verificationStatus
    );
}