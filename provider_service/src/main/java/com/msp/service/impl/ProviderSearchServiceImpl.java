package com.msp.service.impl;

import com.msp.dto.request.ProviderSearchRequest;
import com.msp.dto.response.ProviderSearchResponse;
import com.msp.entity.ProviderAddress;
import com.msp.entity.ProviderSkill;
import com.msp.enums.ProviderSearchSort;
import com.msp.exception.BadRequestException;
import com.msp.repository.ProviderAddressRepository;
import com.msp.repository.ProviderSkillRepository;
import com.msp.service.ProviderSearchService;
import com.msp.specification.ProviderSearchSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProviderSearchServiceImpl implements ProviderSearchService {

    private final ProviderSkillRepository providerSkillRepository;
    private final ProviderAddressRepository providerAddressRepository;

    @Override
    public Page<ProviderSearchResponse> searchProviders(
            ProviderSearchRequest request
    ) {
        validateRequest(request);

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                buildSort(request.getSort())
        );

        Page<ProviderSkill> skills =
                providerSkillRepository.findAll(
                        ProviderSearchSpecification.build(request),
                        pageable
                );

        return skills.map(this::mapToResponse);
    }

    private void validateRequest(ProviderSearchRequest request) {

        if (request.getMinPrice() != null
                && request.getMaxPrice() != null
                && request.getMinPrice() > request.getMaxPrice()) {

        	throw new BadRequestException(
        	        "Minimum price cannot be greater than maximum price"
        	);
        }

        boolean hasLatitude = request.getLatitude() != null;
        boolean hasLongitude = request.getLongitude() != null;
        boolean hasRadius = request.getRadiusKm() != null;

        boolean anyLocationValue =
                hasLatitude || hasLongitude || hasRadius;

        boolean allLocationValues =
                hasLatitude && hasLongitude && hasRadius;

        if (anyLocationValue && !allLocationValues) {
        	throw new BadRequestException(
                    "Latitude, longitude and radiusKm must be provided together"
            );
        }

        /*
         * Radius search needs a dedicated distance query.
         * We reject it for now instead of returning inaccurate results.
         */
        if (allLocationValues) {
        	 throw new BadRequestException(
                     "Radius-based provider search is not available yet"
             );
        }

        if (request.getSort() == ProviderSearchSort.DISTANCE_ASC) {
        	 throw new BadRequestException(
                     "Distance sorting is not available yet"
             );
        }
    }

    private Sort buildSort(ProviderSearchSort sort) {

        if (sort == null) {
            sort = ProviderSearchSort.RATING_DESC;
        }

        return switch (sort) {

            case PRICE_ASC ->
                    Sort.by(
                            Sort.Order.asc("basePrice"),
                            Sort.Order.desc("provider.averageRating")
                    );

            case PRICE_DESC ->
                    Sort.by(
                            Sort.Order.desc("basePrice"),
                            Sort.Order.desc("provider.averageRating")
                    );

            case EXPERIENCE_DESC ->
                    Sort.by(
                            Sort.Order.desc("provider.experienceYears"),
                            Sort.Order.desc("provider.averageRating")
                    );

            case JOBS_DESC ->
                    Sort.by(
                            Sort.Order.desc("provider.totalJobsCompleted"),
                            Sort.Order.desc("provider.averageRating")
                    );

            case RATING_DESC ->
                    Sort.by(
                            Sort.Order.desc("provider.averageRating"),
                            Sort.Order.desc("provider.totalJobsCompleted")
                    );

            case DISTANCE_ASC ->
                    throw new IllegalArgumentException(
                            "Distance sorting is not available yet"
                    );
        };
    }

    private ProviderSearchResponse mapToResponse(
            ProviderSkill skill
    ) {
        ProviderAddress primaryAddress =
                providerAddressRepository
                        .findFirstByProviderIdAndPrimaryAddressTrue(
                                skill.getProvider().getId()
                        )
                        .orElse(null);

        return ProviderSearchResponse.builder()
                .providerId(skill.getProvider().getId())
                .fullName(skill.getProvider().getFullName())
                .profileImage(skill.getProvider().getProfileImage())
                .description(skill.getProvider().getDescription())
                .experienceYears(
                        skill.getProvider().getExperienceYears()
                )
                .averageRating(
                        skill.getProvider().getAverageRating()
                )
                .totalJobsCompleted(
                        skill.getProvider().getTotalJobsCompleted()
                )
                .skillId(skill.getId())
                .categoryId(skill.getCategory().getId())
                .categoryName(skill.getCategory().getName())
                .basePrice(skill.getBasePrice())
                .city(
                        primaryAddress != null
                                ? primaryAddress.getCity()
                                : null
                )
                .state(
                        primaryAddress != null
                                ? primaryAddress.getState()
                                : null
                )
                .distanceKm(null)
                .build();
    }
}