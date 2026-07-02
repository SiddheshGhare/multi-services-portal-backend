package com.msp.controller;

import com.msp.dto.request.CreateDocumentRequest;
import com.msp.entity.ProviderDocument;
import com.msp.enums.DocumentType;
import com.msp.service.CloudinaryService;
import com.msp.service.ProviderDocumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/providers/documents")
@RequiredArgsConstructor
public class ProviderDocumentController {
	private final CloudinaryService cloudinaryService;
    private final ProviderDocumentService documentService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ProviderDocument> uploadDocument(
            @RequestHeader("X-User-Id") Long authUserId,
            @RequestParam("documentType") DocumentType documentType,
            @RequestParam("file") MultipartFile file) {

        return ResponseEntity.ok(
                documentService.uploadDocument(authUserId, documentType, file)
        );
    }

    @PutMapping(value = "/{documentId}", consumes = "multipart/form-data")
    public ResponseEntity<ProviderDocument> updateDocument(
            @RequestHeader("X-User-Id") Long authUserId,
            @PathVariable Long documentId,
            @RequestParam("documentType") DocumentType documentType,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        return ResponseEntity.ok(
                documentService.updateDocument(authUserId, documentId, documentType, file)
        );
    }

    @GetMapping("/{documentId}")
    public ResponseEntity<ProviderDocument> getDocumentById(
            @RequestHeader("X-User-Id") Long authUserId,
            @PathVariable Long documentId) {

        return ResponseEntity.ok(
                documentService.getDocumentById(authUserId, documentId)
        );
    }

    @GetMapping
    public ResponseEntity<List<ProviderDocument>> getAllDocuments(
            @RequestHeader("X-User-Id") Long authUserId) {

        return ResponseEntity.ok(
                documentService.getAllDocuments(authUserId)
        );
    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<String> deleteDocument(
            @RequestHeader("X-User-Id") Long authUserId,
            @PathVariable Long documentId) {

        documentService.deleteDocument(authUserId, documentId);
        return ResponseEntity.ok("Document deleted successfully");
    }
}
