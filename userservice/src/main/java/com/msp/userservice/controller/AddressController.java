package com.msp.userservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.msp.userservice.entity.UserAddress;
import com.msp.userservice.service.UserAddressService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users/address")
public class AddressController {

	@Autowired
	private UserAddressService userAddressService;

	// Add Address
	@PostMapping
	public ResponseEntity<UserAddress> addAddress(@RequestHeader("X-User-Id") Long authUserId,
			@Valid @RequestBody UserAddress address) {

		return ResponseEntity.ok(userAddressService.addAddress(authUserId, address));
	}

	// Get All Addresses
	@GetMapping
	public ResponseEntity<List<UserAddress>> getAllAddresses(@RequestHeader("X-User-Id") Long authUserId) {

		return ResponseEntity.ok(userAddressService.getAllAddresses(authUserId));
	}

	// Get Address By Id
	@GetMapping("/{id}")
	public ResponseEntity<UserAddress> getAddressById(@RequestHeader("X-User-Id") Long authUserId,
			@PathVariable Long id) {

		return ResponseEntity.ok(userAddressService.getAddressById(authUserId, id));
	}

	// Update Address
	@PutMapping("/{id}")
	public ResponseEntity<UserAddress> updateAddress(@RequestHeader("X-User-Id") Long authUserId, @PathVariable Long id,
			@Valid @RequestBody UserAddress address) {

		return ResponseEntity.ok(userAddressService.updateAddress(authUserId, id, address));
	}

	// Delete Address
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteAddress(@RequestHeader("X-User-Id") Long authUserId, @PathVariable Long id) {

		userAddressService.deleteAddress(authUserId, id);

		return ResponseEntity.ok("Address deleted successfully");
	}

	// Make Default Address
	@PutMapping("/{id}/default")
	public ResponseEntity<UserAddress> makeDefault(@RequestHeader("X-User-Id") Long authUserId, @PathVariable Long id) {

		return ResponseEntity.ok(userAddressService.makeDefaultAddress(authUserId, id));
	}
}