package com.msp.service.impl;

import com.msp.dto.request.VerifyDocumentRequest;
import com.msp.entity.ProviderDocument;
import com.msp.enums.VerificationStatus;
import com.msp.repository.ProviderDocumentRepository;
import com.msp.service.AdminProviderDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminProviderDocumentServiceImpl implements AdminProviderDocumentService {

    private final ProviderDocumentRepository providerDocumentRepository;

    @Override
    public List<ProviderDocument> getDocumentsByStatus(VerificationStatus status) {
        return providerDocumentRepository.findByVerificationStatus(status);
    }

    @Override
    public ProviderDocument getDocumentById(Long documentId) {

        return providerDocumentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));
    }

    @Override
    public ProviderDocument approveDocument(Long documentId) {

        ProviderDocument document = getDocumentById(documentId);

        if (document.getVerificationStatus() == VerificationStatus.VERIFIED) {
            throw new RuntimeException("Document is already verified.");
        }

        document.setVerificationStatus(VerificationStatus.VERIFIED);
        document.setRemarks(null);

        return providerDocumentRepository.save(document);
    }

    @Override
    public ProviderDocument rejectDocument(Long documentId,
                                           VerifyDocumentRequest request) {

        ProviderDocument document = getDocumentById(documentId);

        if (document.getVerificationStatus() == VerificationStatus.REJECTED) {
            throw new RuntimeException("Document is already rejected.");
        }

        document.setVerificationStatus(VerificationStatus.REJECTED);
        document.setRemarks(request.getRemarks());

        return providerDocumentRepository.save(document);
    }
}
