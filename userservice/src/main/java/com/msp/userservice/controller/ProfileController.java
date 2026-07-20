package com.msp.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.msp.userservice.entity.UserProfile;
import com.msp.userservice.service.UserProfileService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users/profile")
@Validated
public class ProfileController {

	@Autowired
	private UserProfileService userProfileService;

	// Create Profile
	@PostMapping
	public ResponseEntity<UserProfile> createProfile(@RequestHeader("X-User-Id") Long authUserId,
			@Valid @RequestBody UserProfile userProfile) {

		UserProfile profile = userProfileService.createProfile(authUserId, userProfile);

		return new ResponseEntity<>(profile, HttpStatus.CREATED);
	}

	// Get Profile
	@GetMapping
	public ResponseEntity<UserProfile> getProfile(@RequestHeader("X-User-Id") Long authUserId) {

		return ResponseEntity.ok(userProfileService.getProfile(authUserId));
	}

	// Update Profile
	@PutMapping
	public ResponseEntity<UserProfile> updateProfile(@RequestHeader("X-User-Id") Long authUserId,
			@Valid @RequestBody UserProfile userProfile) {

		return ResponseEntity.ok(userProfileService.updateProfile(authUserId, userProfile));
	}

	// Delete Profile Image
	@DeleteMapping("/image")
	public ResponseEntity<String> deleteProfileImage(@RequestHeader("X-User-Id") Long authUserId) {

		userProfileService.deleteProfileImage(authUserId);

		return ResponseEntity.ok("Profile image deleted successfully.");
	}

	// Update Profile Image
	@PutMapping("/image")
	public ResponseEntity<UserProfile> updateProfileImage(@RequestHeader("X-User-Id") Long authUserId,
			@RequestParam String profileImage) {

		return ResponseEntity.ok(userProfileService.updateProfileImage(authUserId, profileImage));
	}

}