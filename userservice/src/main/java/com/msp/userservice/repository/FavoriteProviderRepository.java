package com.msp.userservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.msp.userservice.entity.FavoriteProvider;
import com.msp.userservice.entity.UserProfile;

@Repository
public interface FavoriteProviderRepository extends JpaRepository<FavoriteProvider, Long> {

    List<FavoriteProvider> findByUserProfile(UserProfile userProfile);

    boolean existsByUserProfileAndProviderId(UserProfile userProfile,Long providerId);

    void deleteByUserProfileAndProviderId(UserProfile userProfile,Long providerId);

}
