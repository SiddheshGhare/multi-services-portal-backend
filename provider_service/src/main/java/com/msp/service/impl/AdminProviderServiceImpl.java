package com.msp.service.impl;

import com.msp.entity.ProviderProfile;
import com.msp.enums.ApprovalStatus;
import com.msp.enums.VerificationStatus;
import com.msp.repository.ProviderDocumentRepository;
import com.msp.repository.ProviderProfileRepository;
import com.msp.service.AdminProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminProviderServiceImpl implements AdminProviderService {

    private final ProviderProfileRepository providerProfileRepository;
    private final ProviderDocumentRepository providerDocumentRepository;

    @Override
    public List<ProviderProfile> getProvidersByStatus(ApprovalStatus status) {
        return providerProfileRepository.findByApprovalStatus(status);
    }

    @Override
    public ProviderProfile getProviderById(Long providerId) {
        return providerProfileRepository.findById(providerId)
                .orElseThrow(() -> new RuntimeException("Provider not found"));
    }

    @Override
    public ProviderProfile approveProvider(Long providerId) {

        ProviderProfile provider = getProviderById(providerId);

        if (provider.getApprovalStatus() == ApprovalStatus.APPROVED) {
            throw new RuntimeException("Provider is already approved");
        }

        boolean hasUnverifiedDocuments =
                providerDocumentRepository.existsByProviderIdAndVerificationStatusNot(
                        provider.getId(),
                        VerificationStatus.VERIFIED
                );

        if (hasUnverifiedDocuments) {
            throw new RuntimeException("Cannot approve provider. Some documents are not verified.");
        }

        provider.setApprovalStatus(ApprovalStatus.APPROVED);

        return providerProfileRepository.save(provider);
    }

    @Override
    public ProviderProfile rejectProvider(Long providerId) {

        ProviderProfile provider = getProviderById(providerId);

        if (provider.getApprovalStatus() == ApprovalStatus.REJECTED) {
            throw new RuntimeException("Provider is already rejected");
        }

        provider.setApprovalStatus(ApprovalStatus.REJECTED);

        return providerProfileRepository.save(provider);
    }
}