package com.msp.service;

import com.msp.dto.request.VerifyDocumentRequest;
import com.msp.entity.ProviderDocument;
import com.msp.enums.VerificationStatus;

import java.util.List;

public interface AdminProviderDocumentService {

    List<ProviderDocument> getDocumentsByStatus(VerificationStatus status);

    ProviderDocument getDocumentById(Long documentId);

    ProviderDocument approveDocument(Long documentId);

    ProviderDocument rejectDocument(Long documentId, VerifyDocumentRequest request);
}