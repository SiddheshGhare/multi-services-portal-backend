package com.msp.service;



import com.msp.entity.ProviderDocument;
import com.msp.enums.DocumentType;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface ProviderDocumentService {

	ProviderDocument uploadDocument(
	        Long authUserId,
	        DocumentType documentType,
	        MultipartFile file
	);

	ProviderDocument updateDocument(
	        Long authUserId,
	        Long documentId,
	        DocumentType documentType,
	        MultipartFile file
	);

    ProviderDocument getDocumentById(Long authUserId, Long documentId);

    List<ProviderDocument> getAllDocuments(Long authUserId);

    void deleteDocument(Long authUserId, Long documentId);
}