package com.msp.repository;

import com.msp.entity.ProviderSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProviderSkillRepository extends JpaRepository<ProviderSkill, Long> {

    List<ProviderSkill> findByProviderId(Long providerId);

    Optional<ProviderSkill> findByProviderIdAndCategoryId(Long providerId, Long categoryId);

    boolean existsByProviderIdAndCategoryId(Long providerId, Long categoryId);
}