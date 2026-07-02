package com.msp.service;

import com.msp.dto.request.CreateSkillRequest;
import com.msp.entity.ProviderSkill;

import java.util.List;

public interface ProviderSkillService {

    ProviderSkill createSkill(Long authUserId, CreateSkillRequest request);

    ProviderSkill updateSkill(Long authUserId, Long skillId, CreateSkillRequest request);

    ProviderSkill getSkillById(Long authUserId, Long skillId);

    List<ProviderSkill> getAllSkills(Long authUserId);

    void deleteSkill(Long authUserId, Long skillId);
}