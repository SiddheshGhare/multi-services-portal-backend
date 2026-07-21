package com.msp.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import com.msp.enums.ProviderSearchSort;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProviderSearchRequest {

    /*
     * Optional category filter.
     */
	@Positive(message = "Category ID must be greater than 0")
	private Long categoryId;

    /*
     * Optional city filter.
     * City matching will be case-insensitive.
     */
    private String city;

    /*
     * Optional price range.
     */
    @PositiveOrZero(message = "Minimum price cannot be negative")
    private Double minPrice;

    @PositiveOrZero(message = "Maximum price cannot be negative")
    private Double maxPrice;

    /*
     * Optional rating filter.
     */
    @DecimalMin(
            value = "0.0",
            message = "Minimum rating cannot be less than 0"
    )
    @DecimalMax(
            value = "5.0",
            message = "Minimum rating cannot be greater than 5"
    )
    private Double minRating;

    /*
     * Optional geographical search parameters.
     *
     * latitude, longitude and radiusKm must be supplied together.
     * That cross-field validation will be handled in the service layer.
     */
    @DecimalMin(
            value = "-90.0",
            message = "Latitude cannot be less than -90"
    )
    @DecimalMax(
            value = "90.0",
            message = "Latitude cannot be greater than 90"
    )
    private Double latitude;

    @DecimalMin(
            value = "-180.0",
            message = "Longitude cannot be less than -180"
    )
    @DecimalMax(
            value = "180.0",
            message = "Longitude cannot be greater than 180"
    )
    private Double longitude;

    @DecimalMin(
            value = "0.1",
            message = "Radius must be at least 0.1 kilometre"
    )
    @DecimalMax(
            value = "100.0",
            message = "Radius cannot exceed 100 kilometres"
    )
    private Double radiusKm;

    /*
     * Supported values will be:
     *
     * ratingDesc
     * priceAsc
     * priceDesc
     * experienceDesc
     * jobsDesc
     * distanceAsc
     */
    private ProviderSearchSort sort = ProviderSearchSort.RATING_DESC;

    /*
     * Pagination starts from page 0.
     */
    @Min(value = 0, message = "Page number cannot be negative")
    private Integer page = 0;

    @Min(value = 1, message = "Page size must be at least 1")
    @Max(value = 50, message = "Page size cannot exceed 50")
    private Integer size = 10;
}