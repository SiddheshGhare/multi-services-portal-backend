package com.msp.service.impl;

import com.msp.dto.request.CreateSkillRequest;
import com.msp.entity.ProviderProfile;
import com.msp.entity.ProviderSkill;
import com.msp.entity.ServiceCategory;
import com.msp.repository.ProviderProfileRepository;
import com.msp.repository.ProviderSkillRepository;
import com.msp.repository.ServiceCategoryRepository;
import com.msp.service.ProviderSkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProviderSkillServiceImpl implements ProviderSkillService {

    private final ProviderSkillRepository providerSkillRepository;
    private final ProviderProfileRepository providerProfileRepository;
    private final ServiceCategoryRepository serviceCategoryRepository;

    @Override
    public ProviderSkill createSkill(Long authUserId, CreateSkillRequest request) {

        ProviderProfile provider = getProvider(authUserId);

        ServiceCategory category = serviceCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Service category not found"));

        boolean alreadyExists = providerSkillRepository
                .existsByProviderIdAndCategoryId(provider.getId(), category.getId());

        if (alreadyExists) {
            throw new RuntimeException("This skill is already added by provider");
        }

        ProviderSkill skill = ProviderSkill.builder()
                .provider(provider)
                .category(category)
                .basePrice(request.getBasePrice())
                .active(true)
                .build();

        return providerSkillRepository.save(skill);
    }

    @Override
    public ProviderSkill updateSkill(Long authUserId, Long skillId, CreateSkillRequest request) {

        ProviderProfile provider = getProvider(authUserId);

        ProviderSkill skill = providerSkillRepository.findById(skillId)
                .orElseThrow(() -> new RuntimeException("Skill not found"));

        validateOwnership(skill, provider);

        ServiceCategory category = serviceCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Service category not found"));

        providerSkillRepository.findByProviderIdAndCategoryId(provider.getId(), category.getId())
                .ifPresent(existingSkill -> {
                    if (!existingSkill.getId().equals(skillId)) {
                        throw new RuntimeException("This skill is already added by provider");
                    }
                });

        skill.setCategory(category);
        skill.setBasePrice(request.getBasePrice());

        return providerSkillRepository.save(skill);
    }

    @Override
    public ProviderSkill getSkillById(Long authUserId, Long skillId) {

        ProviderProfile provider = getProvider(authUserId);

        ProviderSkill skill = providerSkillRepository.findById(skillId)
                .orElseThrow(() -> new RuntimeException("Skill not found"));

        validateOwnership(skill, provider);

        return skill;
    }

    @Override
    public List<ProviderSkill> getAllSkills(Long authUserId) {

        ProviderProfile provider = getProvider(authUserId);

        return providerSkillRepository.findByProviderId(provider.getId());
    }

    @Override
    public void deleteSkill(Long authUserId, Long skillId) {

        ProviderProfile provider = getProvider(authUserId);

        ProviderSkill skill = providerSkillRepository.findById(skillId)
                .orElseThrow(() -> new RuntimeException("Skill not found"));

        validateOwnership(skill, provider);

        providerSkillRepository.delete(skill);
    }

    private ProviderProfile getProvider(Long authUserId) {

        return providerProfileRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new RuntimeException("Provider profile not found"));
    }

    private void validateOwnership(ProviderSkill skill, ProviderProfile provider) {

        if (!skill.getProvider().getId().equals(provider.getId())) {
            throw new RuntimeException("Unauthorized access");
        }
    }
}