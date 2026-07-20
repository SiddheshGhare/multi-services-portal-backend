package com.msp.userservice.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.msp.userservice.entity.UserAddress;
import com.msp.userservice.entity.UserProfile;
import com.msp.userservice.exception.ResourceNotFoundException;
import com.msp.userservice.repository.UserAddressRepository;
import com.msp.userservice.repository.UserProfileRepository;
import com.msp.userservice.service.UserAddressService;

@Service
public class UserAddressServiceImpl implements UserAddressService {

    @Autowired
    private UserAddressRepository userAddressRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    private UserProfile getUserProfile(Long authUserId) {

        return userProfileRepository.findByAuthUserId(authUserId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User profile not found."));
    }

    private UserAddress getUserAddress(UserProfile profile, Long addressId) {

        UserAddress address = userAddressRepository.findById(addressId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Address not found."));

        if (!address.getUserProfile().getId().equals(profile.getId())) {
            throw new ResourceNotFoundException("Address does not belong to user.");
        }

        return address;
    }

    @Override
    public UserAddress addAddress(Long authUserId, UserAddress address) {

        UserProfile profile = getUserProfile(authUserId);

        address.setUserProfile(profile);

        if (Boolean.TRUE.equals(address.getIsDefault())) {

            Optional<UserAddress> defaultAddress =
                    userAddressRepository.findByUserProfileAndIsDefaultTrue(profile);

            if (defaultAddress.isPresent()) {

                UserAddress oldDefault = defaultAddress.get();
                oldDefault.setIsDefault(false);
                userAddressRepository.save(oldDefault);

            }
        }

        return userAddressRepository.save(address);
    }

    @Override
    public List<UserAddress> getAllAddresses(Long authUserId) {

        UserProfile profile = getUserProfile(authUserId);

        return userAddressRepository.findByUserProfile(profile);
    }

    @Override
    public UserAddress getAddressById(Long authUserId, Long addressId) {

        UserProfile profile = getUserProfile(authUserId);

        return getUserAddress(profile, addressId);
    }
    @Override
    public UserAddress updateAddress(Long authUserId, Long addressId, UserAddress address) {

        UserProfile profile = getUserProfile(authUserId);

        UserAddress existingAddress = getUserAddress(profile, addressId);

        existingAddress.setAddressLine1(address.getAddressLine1());
        existingAddress.setAddressLine2(address.getAddressLine2());
        existingAddress.setLandmark(address.getLandmark());
        existingAddress.setCity(address.getCity());
        existingAddress.setState(address.getState());
        existingAddress.setPincode(address.getPincode());
        existingAddress.setLatitude(address.getLatitude());
        existingAddress.setLongitude(address.getLongitude());
        existingAddress.setAddressType(address.getAddressType());

        if (Boolean.TRUE.equals(address.getIsDefault())) {

            Optional<UserAddress> defaultAddress =
                    userAddressRepository.findByUserProfileAndIsDefaultTrue(profile);

            if (defaultAddress.isPresent()) {

                UserAddress oldDefault = defaultAddress.get();

                if (!oldDefault.getId().equals(existingAddress.getId())) {

                    oldDefault.setIsDefault(false);
                    userAddressRepository.save(oldDefault);

                }
            }

            existingAddress.setIsDefault(true);
        }

        return userAddressRepository.save(existingAddress);
    }

    @Override
    public void deleteAddress(Long authUserId, Long addressId) {

        UserProfile profile = getUserProfile(authUserId);

        UserAddress address = getUserAddress(profile, addressId);

        userAddressRepository.delete(address);
    }

    @Override
    public UserAddress makeDefaultAddress(Long authUserId, Long addressId) {

        UserProfile profile = getUserProfile(authUserId);

        UserAddress newDefault = getUserAddress(profile, addressId);

        Optional<UserAddress> oldDefault =
                userAddressRepository.findByUserProfileAndIsDefaultTrue(profile);

        if (oldDefault.isPresent()) {

            UserAddress address = oldDefault.get();

            address.setIsDefault(false);

            userAddressRepository.save(address);
        }

        newDefault.setIsDefault(true);

        return userAddressRepository.save(newDefault);
    }

}