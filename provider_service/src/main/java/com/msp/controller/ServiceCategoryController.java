package com.msp.controller;

import com.msp.dto.request.CreateServiceCategoryRequest;
import com.msp.entity.ServiceCategory;
import com.msp.service.ServiceCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/providers/service-categories")
@RequiredArgsConstructor
public class ServiceCategoryController {

    private final ServiceCategoryService categoryService;

    @PostMapping
    public ResponseEntity<ServiceCategory> createCategory(
            @RequestHeader("X-User-Role") String role,
            @Valid @RequestBody CreateServiceCategoryRequest request) {

        validateAdmin(role);
        return ResponseEntity.ok(categoryService.createCategory(request));
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<ServiceCategory> updateCategory(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long categoryId,
            @Valid @RequestBody CreateServiceCategoryRequest request) {

        validateAdmin(role);
        return ResponseEntity.ok(categoryService.updateCategory(categoryId, request));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<ServiceCategory> getCategoryById(
            @PathVariable Long categoryId) {

        return ResponseEntity.ok(categoryService.getCategoryById(categoryId));
    }

    @GetMapping
    public ResponseEntity<List<ServiceCategory>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteCategory(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long categoryId) {

        validateAdmin(role);
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok("Service category deleted successfully");
    }

    private void validateAdmin(String role) {
        if (!"ADMIN".equalsIgnoreCase(role)) {
            throw new RuntimeException("Access denied: Admin only");
        }
    }
}