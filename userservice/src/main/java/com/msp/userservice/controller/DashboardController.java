package com.msp.userservice.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.msp.userservice.service.DashboardService;

@RestController
@RequestMapping("/api/users/dashboard")
public class DashboardController {

	@Autowired
	private DashboardService dashboardService;

	// Get User Dashboard
	@GetMapping
	public ResponseEntity<Map<String, Object>> getDashboard(@RequestHeader("X-User-Id") Long authUserId) {

		Map<String, Object> dashboard = dashboardService.getDashboard(authUserId);

		return ResponseEntity.ok(dashboard);
	}

}