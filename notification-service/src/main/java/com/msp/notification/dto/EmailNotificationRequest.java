package com.msp.notification.dto;

import com.msp.notification.template.EmailTemplate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailNotificationRequest {

    @Email
    @NotBlank
    private String to;

    @NotBlank
    private String subject;

    @NotNull
    private EmailTemplate template;

    @Builder.Default
    private Map<String, Object> data = new HashMap<>();
}
