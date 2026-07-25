package com.msp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthUserSummaryResponse {

    private Long id;
    private String email;
    private String role;
}
