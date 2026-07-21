package com.msp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProviderSearchResponse {

    /*
     * Provider information
     */
    private Long providerId;
    private String fullName;
    private String profileImage;
    private String description;
    private Integer experienceYears;
    private Double averageRating;
    private Integer totalJobsCompleted;

    /*
     * Skill and category information
     */
    private Long skillId;
    private Long categoryId;
    private String categoryName;
    private Double basePrice;

    /*
     * General location information
     */
    private String city;
    private String state;

    /*
     * Populated only when latitude, longitude and radius are supplied.
     * It remains null for ordinary city/category searches.
     */
    private Double distanceKm;
}