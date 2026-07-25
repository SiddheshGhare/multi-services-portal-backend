package com.msp.service.impl;

import com.msp.client.AuthServiceClient;
import com.msp.client.NotificationServiceClient;
import com.msp.dto.request.EmailNotificationRequest;
import com.msp.dto.response.AuthUserSummaryResponse;
import com.msp.dto.response.NotificationResponse;
import com.msp.entity.ProviderDocument;
import com.msp.entity.ProviderProfile;
import com.msp.service.ProviderNotificationDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProviderNotificationDispatcherImpl implements ProviderNotificationDispatcher {

    private final NotificationServiceClient notificationServiceClient;
    private final AuthServiceClient authServiceClient;

    @Override
    public void sendProviderApproved(ProviderProfile provider) {
        sendNotification(provider.getAuthUserId(), "Provider Approved", "PROVIDER_APPROVED", baseProviderData(provider));
    }

    @Override
    public void sendProviderRejected(ProviderProfile provider) {
        sendNotification(provider.getAuthUserId(), "Provider Rejected", "PROVIDER_REJECTED", baseProviderData(provider));
    }

    @Override
    public void sendDocumentApproved(ProviderDocument document) {
        Map<String, Object> data = baseProviderData(document.getProvider());
        data.put("documentType", document.getDocumentType().name());
        sendNotification(document.getProvider().getAuthUserId(), "Document Approved", "DOCUMENT_APPROVED", data);
    }

    @Override
    public void sendDocumentRejected(ProviderDocument document) {
        Map<String, Object> data = baseProviderData(document.getProvider());
        data.put("documentType", document.getDocumentType().name());
        data.put("remarks", document.getRemarks());
        sendNotification(document.getProvider().getAuthUserId(), "Document Rejected", "DOCUMENT_REJECTED", data);
    }

    private Map<String, Object> baseProviderData(ProviderProfile provider) {
        Map<String, Object> data = new HashMap<>();
        data.put("providerName", provider.getFullName());
        return data;
    }

    private void sendNotification(Long authUserId, String subject, String template, Map<String, Object> data) {
        try {
            AuthUserSummaryResponse user = authServiceClient.getUserById(authUserId);
            NotificationResponse response = notificationServiceClient.sendEmail(
                    EmailNotificationRequest.builder()
                            .to(user.getEmail())
                            .subject(subject)
                            .template(template)
                            .data(data)
                            .build()
            );

            if (!response.isSuccess()) {
                log.warn("Notification service returned failure for template {} and user {}: {}", template, authUserId, response.getMessage());
            }
        } catch (Exception ex) {
            log.warn("Failed to send notification for template {} and user {}", template, authUserId, ex);
        }
    }
}
