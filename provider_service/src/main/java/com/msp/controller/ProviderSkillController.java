package com.msp.controller;

import com.msp.dto.request.CreateSkillRequest;
import com.msp.entity.ProviderSkill;
import com.msp.service.ProviderSkillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/providers/skills")
@RequiredArgsConstructor
public class ProviderSkillController {

    private final ProviderSkillService providerSkillService;

    @PostMapping
    public ResponseEntity<ProviderSkill> createSkill(
            @RequestHeader("X-User-Id") Long authUserId,
            @Valid @RequestBody CreateSkillRequest request) {

        ProviderSkill skill = providerSkillService.createSkill(authUserId, request);
        return ResponseEntity.ok(skill);
    }

    @PutMapping("/{skillId}")
    public ResponseEntity<ProviderSkill> updateSkill(
            @RequestHeader("X-User-Id") Long authUserId,
            @PathVariable Long skillId,
            @Valid @RequestBody CreateSkillRequest request) {

        ProviderSkill skill = providerSkillService.updateSkill(authUserId, skillId, request);
        return ResponseEntity.ok(skill);
    }

    @GetMapping("/{skillId}")
    public ResponseEntity<ProviderSkill> getSkillById(
            @RequestHeader("X-User-Id") Long authUserId,
            @PathVariable Long skillId) {

        ProviderSkill skill = providerSkillService.getSkillById(authUserId, skillId);
        return ResponseEntity.ok(skill);
    }

    @GetMapping
    public ResponseEntity<List<ProviderSkill>> getAllSkills(
            @RequestHeader("X-User-Id") Long authUserId) {

        List<ProviderSkill> skills = providerSkillService.getAllSkills(authUserId);
        return ResponseEntity.ok(skills);
    }

    @DeleteMapping("/{skillId}")
    public ResponseEntity<String> deleteSkill(
            @RequestHeader("X-User-Id") Long authUserId,
            @PathVariable Long skillId) {

        providerSkillService.deleteSkill(authUserId, skillId);
        return ResponseEntity.ok("Skill deleted successfully");
    }
}