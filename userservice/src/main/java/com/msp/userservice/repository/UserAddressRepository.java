package com.msp.userservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.msp.userservice.entity.UserAddress;
import com.msp.userservice.entity.UserProfile;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {

    List<UserAddress> findByUserProfile(UserProfile userProfile);

    Optional<UserAddress> findByUserProfileAndIsDefaultTrue(UserProfile userProfile);

}