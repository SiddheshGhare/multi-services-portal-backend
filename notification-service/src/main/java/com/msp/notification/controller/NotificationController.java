package com.msp.notification.controller;

import com.msp.notification.dto.EmailNotificationRequest;
import com.msp.notification.dto.NotificationResponse;
import com.msp.notification.service.EmailNotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final EmailNotificationService emailNotificationService;

    @PostMapping("/email")
    public ResponseEntity<NotificationResponse> sendEmail(@Valid @RequestBody EmailNotificationRequest request) {
        return ResponseEntity.ok(emailNotificationService.sendEmail(request));
    }
}
