package com.msp.controller;

import com.msp.dto.common.ApiResponse;
import com.msp.dto.request.ProviderSearchRequest;
import com.msp.dto.response.ProviderSearchResponse;
import com.msp.service.ProviderSearchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/providers")
@RequiredArgsConstructor
public class ProviderSearchController {

    private final ProviderSearchService providerSearchService;

    @GetMapping("/search")
    public ApiResponse<Page<ProviderSearchResponse>> searchProviders(
            @Valid @ModelAttribute ProviderSearchRequest request
    ) {
        return ApiResponse.<Page<ProviderSearchResponse>>builder()
                .success(true)
                .message("Providers fetched successfully")
                .data(providerSearchService.searchProviders(request))
                .build();
    }
}