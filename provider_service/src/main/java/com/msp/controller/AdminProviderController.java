package com.msp.controller;

import com.msp.entity.ProviderProfile;
import com.msp.enums.ApprovalStatus;
import com.msp.service.AdminProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/providers")
@RequiredArgsConstructor
public class AdminProviderController {

    private final AdminProviderService adminProviderService;

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ProviderProfile>> getProvidersByStatus(
            @RequestHeader("X-User-Role") String role,
            @PathVariable ApprovalStatus status) {

        validateAdmin(role);
        return ResponseEntity.ok(adminProviderService.getProvidersByStatus(status));
    }

    @GetMapping("/{providerId}")
    public ResponseEntity<ProviderProfile> getProviderById(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long providerId) {

        validateAdmin(role);
        return ResponseEntity.ok(adminProviderService.getProviderById(providerId));
    }

    @PutMapping("/{providerId}/approve")
    public ResponseEntity<ProviderProfile> approveProvider(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long providerId) {

        validateAdmin(role);
        return ResponseEntity.ok(adminProviderService.approveProvider(providerId));
    }

    @PutMapping("/{providerId}/reject")
    public ResponseEntity<ProviderProfile> rejectProvider(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long providerId) {

        validateAdmin(role);
        return ResponseEntity.ok(adminProviderService.rejectProvider(providerId));
    }

    private void validateAdmin(String role) {
        if (!"ADMIN".equalsIgnoreCase(role)) {
            throw new RuntimeException("Access denied: Admin only");
        }
    }
}