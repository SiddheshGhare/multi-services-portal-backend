package com.msp.controller;

import com.msp.dto.request.VerifyDocumentRequest;
import com.msp.entity.ProviderDocument;
import com.msp.enums.VerificationStatus;
import com.msp.service.AdminProviderDocumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/providers/provider-documents")
@RequiredArgsConstructor
public class AdminProviderDocumentController {

    private final AdminProviderDocumentService documentService;

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ProviderDocument>> getDocumentsByStatus(
            @RequestHeader("X-User-Role") String role,
            @PathVariable VerificationStatus status) {

        validateAdmin(role);
        return ResponseEntity.ok(documentService.getDocumentsByStatus(status));
    }

    @GetMapping("/{documentId}")
    public ResponseEntity<ProviderDocument> getDocumentById(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long documentId) {

        validateAdmin(role);
        return ResponseEntity.ok(documentService.getDocumentById(documentId));
    }

    @PutMapping("/{documentId}/approve")
    public ResponseEntity<ProviderDocument> approveDocument(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long documentId) {

        validateAdmin(role);
        return ResponseEntity.ok(documentService.approveDocument(documentId));
    }

    @PutMapping("/{documentId}/reject")
    public ResponseEntity<ProviderDocument> rejectDocument(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long documentId,
            @Valid @RequestBody VerifyDocumentRequest request) {

        validateAdmin(role);
        return ResponseEntity.ok(documentService.rejectDocument(documentId, request));
    }

    private void validateAdmin(String role) {
        if (!"ADMIN".equalsIgnoreCase(role)) {
            throw new RuntimeException("Access denied: Admin only");
        }
    }
}