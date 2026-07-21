package com.msp.repository;

import com.msp.entity.ProviderAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProviderAddressRepository
        extends JpaRepository<ProviderAddress, Long> {

    List<ProviderAddress> findByProviderId(Long providerId);

    Optional<ProviderAddress>
    findFirstByProviderIdAndPrimaryAddressTrue(Long providerId);
}