package com.msp.client;

import com.msp.dto.request.EmailNotificationRequest;
import com.msp.dto.response.NotificationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "NOTIFICATION-SERVICE")
public interface NotificationServiceClient {

    @PostMapping("/api/notifications/email")
    NotificationResponse sendEmail(@RequestBody EmailNotificationRequest request);
}
