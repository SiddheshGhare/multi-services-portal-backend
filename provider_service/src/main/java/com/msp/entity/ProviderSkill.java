package com.msp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "provider_skill")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProviderSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long providerId;

    private Long serviceCategoryId;

    private Double basePrice;
}