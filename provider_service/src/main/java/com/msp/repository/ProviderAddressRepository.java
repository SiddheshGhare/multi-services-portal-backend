package com.msp.repository;

import com.msp.entity.ProviderAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProviderAddressRepository extends JpaRepository<ProviderAddress, Long> {

    List<ProviderAddress> findByProviderId(Long providerId);
}
