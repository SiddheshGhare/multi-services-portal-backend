package com.msp.repository;

import com.msp.entity.ProviderDocument;
import com.msp.enums.VerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProviderDocumentRepository extends JpaRepository<ProviderDocument, Long> {

    List<ProviderDocument> findByProviderId(Long providerId);

    List<ProviderDocument> findByVerificationStatus(VerificationStatus status);
}