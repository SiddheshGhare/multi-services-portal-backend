package com.msp.specification;

import com.msp.dto.request.ProviderSearchRequest;
import com.msp.entity.ProviderAddress;
import com.msp.entity.ProviderProfile;
import com.msp.entity.ProviderSkill;
import com.msp.entity.ServiceCategory;
import com.msp.enums.ApprovalStatus;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class ProviderSearchSpecification {

    private ProviderSearchSpecification() {
        /*
         * Utility class: prevent object creation.
         */
    }

    public static Specification<ProviderSkill> build(
            ProviderSearchRequest request
    ) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            /*
             * ProviderSkill -> ProviderProfile
             */
            Join<ProviderSkill, ProviderProfile> providerJoin =
                    root.join("provider", JoinType.INNER);

            /*
             * ProviderSkill -> ServiceCategory
             */
            Join<ProviderSkill, ServiceCategory> categoryJoin =
                    root.join("category", JoinType.INNER);

            /*
             * Mandatory conditions:
             *
             * 1. Provider account must be approved.
             * 2. Provider skill must be active.
             * 3. Service category must be active.
             */
            predicates.add(
                    criteriaBuilder.equal(
                            providerJoin.get("approvalStatus"),
                            ApprovalStatus.APPROVED
                    )
            );

            predicates.add(
                    criteriaBuilder.isTrue(root.get("active"))
            );

            predicates.add(
                    criteriaBuilder.isTrue(categoryJoin.get("active"))
            );

            /*
             * Optional category filter.
             */
            if (request.getCategoryId() != null) {
                predicates.add(
                        criteriaBuilder.equal(
                                categoryJoin.get("id"),
                                request.getCategoryId()
                        )
                );
            }

            /*
             * Optional minimum price.
             */
            if (request.getMinPrice() != null) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get("basePrice"),
                                request.getMinPrice()
                        )
                );
            }

            /*
             * Optional maximum price.
             */
            if (request.getMaxPrice() != null) {
                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(
                                root.get("basePrice"),
                                request.getMaxPrice()
                        )
                );
            }

            /*
             * Optional minimum provider rating.
             */
            if (request.getMinRating() != null) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(
                                providerJoin.get("averageRating"),
                                request.getMinRating()
                        )
                );
            }

            /*
             * ProviderProfile does not contain an address relationship.
             *
             * Therefore, we use an EXISTS subquery against ProviderAddress
             * rather than incorrectly trying:
             *
             * providerJoin.join("addresses")
             *
             * Search uses only the provider's primary address.
             */
            if (request.getCity() != null
                    && !request.getCity().isBlank()) {

                Subquery<Long> addressSubquery =
                        query.subquery(Long.class);

                Root<ProviderAddress> addressRoot =
                        addressSubquery.from(ProviderAddress.class);

                String normalizedCity =
                        request.getCity().trim().toLowerCase();

                addressSubquery.select(addressRoot.get("id"));

                addressSubquery.where(
                        criteriaBuilder.equal(
                                addressRoot.get("provider").get("id"),
                                providerJoin.get("id")
                        ),
                        criteriaBuilder.isTrue(
                                addressRoot.get("primaryAddress")
                        ),
                        criteriaBuilder.equal(
                                criteriaBuilder.lower(
                                        addressRoot.get("city")
                                ),
                                normalizedCity
                        )
                );

                predicates.add(
                        criteriaBuilder.exists(addressSubquery)
                );
            }

            return criteriaBuilder.and(
                    predicates.toArray(new Predicate[0])
            );
        };
    }
}