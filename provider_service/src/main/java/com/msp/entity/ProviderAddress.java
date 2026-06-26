package com.msp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "provider_address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProviderAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long providerId;

    private String city;

    private String state;

    private String pincode;

    private Double latitude;

    private Double longitude;
}