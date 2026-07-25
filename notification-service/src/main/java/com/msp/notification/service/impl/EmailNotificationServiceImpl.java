package com.msp.notification.service.impl;

import com.msp.notification.dto.EmailNotificationRequest;
import com.msp.notification.dto.NotificationResponse;
import com.msp.notification.service.EmailNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailNotificationServiceImpl implements EmailNotificationService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${app.mail.from:${spring.mail.username:noreply@msp.local}}")
    private String fromEmail;

    @Override
    public NotificationResponse sendEmail(EmailNotificationRequest request) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    true,
                    StandardCharsets.UTF_8.name()
            );

            helper.setFrom(fromEmail);
            helper.setTo(request.getTo());
            helper.setSubject(request.getSubject());
            helper.setText(buildHtml(request), true);

            mailSender.send(message);

            return new NotificationResponse(true, "Email sent successfully");
        } catch (Exception ex) {
            log.error("Failed to send email for template {}", request.getTemplate(), ex);
            return new NotificationResponse(false, "Email sending failed: " + ex.getMessage());
        }
    }

    private String buildHtml(EmailNotificationRequest request) {
        Context context = new Context();

        Map<String, Object> variables = new HashMap<>(request.getData());
        variables.putIfAbsent("subject", request.getSubject());
        variables.putIfAbsent("supportEmail", fromEmail);

        context.setVariables(variables);

        return templateEngine.process(request.getTemplate().getTemplateName(), context);
    }
}
