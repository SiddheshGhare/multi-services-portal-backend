package com.msp.controller;

import com.msp.dto.AuthUserSummaryResponse;
import com.msp.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/internal/users")
@RequiredArgsConstructor
public class InternalUserController {

    private final AuthService authService;

    @GetMapping("/{userId}")
    public ResponseEntity<AuthUserSummaryResponse> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(authService.getUserSummaryById(userId));
    }
}
