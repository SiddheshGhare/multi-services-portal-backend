package com.msp.service;

import com.msp.dto.request.ProviderSearchRequest;
import com.msp.dto.response.ProviderSearchResponse;
import org.springframework.data.domain.Page;

public interface ProviderSearchService {

    Page<ProviderSearchResponse> searchProviders(
            ProviderSearchRequest request
    );
}