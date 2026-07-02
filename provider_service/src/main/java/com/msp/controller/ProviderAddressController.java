package com.msp.controller;

import com.msp.dto.request.CreateAddressRequest;
import com.msp.entity.ProviderAddress;
import com.msp.service.ProviderAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/providers/addresses")
@RequiredArgsConstructor
public class ProviderAddressController {

    private final ProviderAddressService addressService;

    // CREATE
    @PostMapping
    public ResponseEntity<ProviderAddress> createAddress(
            @RequestHeader("X-User-Id") Long authUserId,
            @Valid @RequestBody CreateAddressRequest request) {

        return ResponseEntity.ok(
                addressService.createAddress(authUserId, request)
        );
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ProviderAddress> updateAddress(
            @RequestHeader("X-User-Id") Long authUserId,
            @PathVariable Long id,
            @Valid @RequestBody CreateAddressRequest request) {

        return ResponseEntity.ok(
                addressService.updateAddress(authUserId, id, request)
        );
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ProviderAddress> getAddressById(
            @RequestHeader("X-User-Id") Long authUserId,
            @PathVariable Long id) {

        return ResponseEntity.ok(
                addressService.getAddressById(authUserId, id)
        );
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<ProviderAddress>> getAllAddresses(
            @RequestHeader("X-User-Id") Long authUserId) {

        return ResponseEntity.ok(
                addressService.getAllAddresses(authUserId)
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAddress(
            @RequestHeader("X-User-Id") Long authUserId,
            @PathVariable Long id) {

        addressService.deleteAddress(authUserId, id);
        return ResponseEntity.ok("Address deleted successfully");
    }
}