package com.msp.service;

import com.msp.entity.ProviderProfile;
import com.msp.enums.ApprovalStatus;

import java.util.List;

public interface AdminProviderService {

    List<ProviderProfile> getProvidersByStatus(ApprovalStatus status);

    ProviderProfile getProviderById(Long providerId);

    ProviderProfile approveProvider(Long providerId);

    ProviderProfile rejectProvider(Long providerId);
}