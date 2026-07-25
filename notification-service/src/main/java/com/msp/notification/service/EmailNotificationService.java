package com.msp.notification.service;

import com.msp.notification.dto.EmailNotificationRequest;
import com.msp.notification.dto.NotificationResponse;

public interface EmailNotificationService {

    NotificationResponse sendEmail(EmailNotificationRequest request);
}
