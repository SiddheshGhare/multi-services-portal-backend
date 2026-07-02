package com.msp.service.impl;


import com.msp.entity.ProviderDocument;
import com.msp.entity.ProviderProfile;
import com.msp.repository.ProviderDocumentRepository;
import com.msp.repository.ProviderProfileRepository;
import com.msp.service.CloudinaryService;
import com.msp.service.ProviderDocumentService;
import com.msp.enums.DocumentType;
import com.msp.enums.VerificationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProviderDocumentServiceImpl implements ProviderDocumentService {
	private final CloudinaryService cloudinaryService;
    private final ProviderDocumentRepository documentRepository;
    private final ProviderProfileRepository providerProfileRepository;

    @Override
    public ProviderDocument uploadDocument(
            Long authUserId,
            DocumentType documentType,
            MultipartFile file
    ) {
        ProviderProfile provider = providerProfileRepository
                .findByAuthUserId(authUserId)
                .orElseThrow(() -> new RuntimeException("Provider profile not found"));

        String uploadedUrl = cloudinaryService.uploadFile(file);

        ProviderDocument document = ProviderDocument.builder()
                .provider(provider)
                .documentType(documentType)
                .documentUrl(uploadedUrl)
                .verificationStatus(VerificationStatus.PENDING)
                .build();

        return documentRepository.save(document);
    }

    @Override
    public ProviderDocument updateDocument(
            Long authUserId,
            Long documentId,
            DocumentType documentType,
            MultipartFile file
    ) {
        ProviderDocument document = getDocumentById(authUserId, documentId);

        document.setDocumentType(documentType);

        if (file != null && !file.isEmpty()) {
            String uploadedUrl = cloudinaryService.uploadFile(file);

            document.setDocumentUrl(uploadedUrl);
            document.setVerificationStatus(VerificationStatus.PENDING);
            document.setRemarks(null);
        }

        return documentRepository.save(document);
    }
    @Override
    public ProviderDocument getDocumentById(Long authUserId,
                                            Long documentId) {

        ProviderProfile provider = getProvider(authUserId);

        ProviderDocument document = getDocument(documentId);

        validateOwnership(document, provider);

        return document;
    }

    @Override
    public List<ProviderDocument> getAllDocuments(Long authUserId) {

        ProviderProfile provider = getProvider(authUserId);

        return documentRepository.findByProviderId(provider.getId());
    }

    @Override
    public void deleteDocument(Long authUserId,
                               Long documentId) {

        ProviderProfile provider = getProvider(authUserId);

        ProviderDocument document = getDocument(documentId);

        validateOwnership(document, provider);

        documentRepository.delete(document);
    }

    // ---------------- PRIVATE METHODS ----------------

    private ProviderProfile getProvider(Long authUserId) {

        return providerProfileRepository.findByAuthUserId(authUserId)
                .orElseThrow(() ->
                        new RuntimeException("Provider profile not found"));
    }

    private ProviderDocument getDocument(Long id) {

        return documentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Document not found"));
    }

    private void validateOwnership(ProviderDocument document,
                                   ProviderProfile provider) {

        if (!document.getProvider().getId().equals(provider.getId())) {
            throw new RuntimeException("Unauthorized access");
        }
    }
}