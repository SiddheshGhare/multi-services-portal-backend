package com.msp.repository;

import com.msp.entity.ProviderSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProviderSkillRepository extends JpaRepository<ProviderSkill, Long> {

    List<ProviderSkill> findByProviderId(Long providerId);
}
