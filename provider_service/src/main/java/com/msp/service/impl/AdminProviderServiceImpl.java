package com.msp.service.impl;

import com.msp.entity.ProviderProfile;
import com.msp.enums.ApprovalStatus;
import com.msp.enums.VerificationStatus;
import com.msp.repository.ProviderDocumentRepository;
import com.msp.repository.ProviderProfileRepository;
import com.msp.service.AdminProviderService;
import com.msp.service.ProviderNotificationDispatcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminProviderServiceImpl implements AdminProviderService {

    private final ProviderProfileRepository providerProfileRepository;
    private final ProviderDocumentRepository providerDocumentRepository;
    private final ProviderNotificationDispatcher providerNotificationDispatcher;

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

        ProviderProfile savedProvider = providerProfileRepository.save(provider);
        providerNotificationDispatcher.sendProviderApproved(savedProvider);
        return savedProvider;
    }

    @Override
    public ProviderProfile rejectProvider(Long providerId) {

        ProviderProfile provider = getProviderById(providerId);

        if (provider.getApprovalStatus() == ApprovalStatus.REJECTED) {
            throw new RuntimeException("Provider is already rejected");
        }

        provider.setApprovalStatus(ApprovalStatus.REJECTED);

        ProviderProfile savedProvider = providerProfileRepository.save(provider);
        providerNotificationDispatcher.sendProviderRejected(savedProvider);
        return savedProvider;
    }
}
