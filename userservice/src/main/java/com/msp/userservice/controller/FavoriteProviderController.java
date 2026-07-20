package com.msp.userservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.msp.userservice.entity.FavoriteProvider;
import com.msp.userservice.service.FavoriteProviderService;

@RestController
@RequestMapping("/api/users/favorites")
public class FavoriteProviderController {

	@Autowired
	private FavoriteProviderService favoriteProviderService;

	// Add Favourite Provider
	@PostMapping("/{providerId}")
	public ResponseEntity<FavoriteProvider> addFavorite(@RequestHeader("X-User-Id") Long authUserId,
			@PathVariable Long providerId) {

		FavoriteProvider favoriteProvider = favoriteProviderService.addFavorite(authUserId, providerId);

		return ResponseEntity.ok(favoriteProvider);
	}

	// Get All Favourite Providers
	@GetMapping
	public ResponseEntity<List<FavoriteProvider>> getFavorites(@RequestHeader("X-User-Id") Long authUserId) {

		List<FavoriteProvider> favorites = favoriteProviderService.getFavorites(authUserId);

		return ResponseEntity.ok(favorites);
	}

	// Remove Favourite Provider
	@DeleteMapping("/{providerId}")
	public ResponseEntity<String> removeFavorite(@RequestHeader("X-User-Id") Long authUserId,
			@PathVariable Long providerId) {

		favoriteProviderService.removeFavorite(authUserId, providerId);

		return ResponseEntity.ok("Favourite provider removed successfully.");
	}

}