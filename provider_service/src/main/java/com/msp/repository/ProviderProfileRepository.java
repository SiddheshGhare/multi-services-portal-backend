package com.msp.repository;

import com.msp.entity.ProviderProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProviderProfileRepository extends JpaRepository<ProviderProfile, Long> {

    ProviderProfile findByAuthUserId(Long authUserId);
}
