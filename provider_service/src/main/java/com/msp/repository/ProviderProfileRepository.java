package com.msp.repository;

import com.msp.entity.ProviderProfile;
import com.msp.enums.ApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProviderProfileRepository extends JpaRepository<ProviderProfile, Long> {

    Optional<ProviderProfile> findByAuthUserId(Long authUserId);

    boolean existsByAuthUserId(Long authUserId);

    List<ProviderProfile> findByApprovalStatus(ApprovalStatus status);
}