package com.msp.userservice.serviceimpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.msp.userservice.entity.UserProfile;
import com.msp.userservice.exception.DuplicateResourceException;
import com.msp.userservice.exception.ResourceNotFoundException;
import com.msp.userservice.repository.UserProfileRepository;
import com.msp.userservice.service.UserProfileService;

@Service
public class UserProfileServiceImpl implements UserProfileService {

	@Autowired
	private UserProfileRepository userProfileRepository;

	@Override
	public UserProfile createProfile(Long authUserId, UserProfile userProfile) {

		if (userProfileRepository.existsByAuthUserId(authUserId)) {
			throw new DuplicateResourceException("Profile already exists.");
		}

		userProfile.setAuthUserId(authUserId);

		return userProfileRepository.save(userProfile);
	}

	@Override
	public UserProfile getProfile(Long authUserId) {

		Optional<UserProfile> profile = userProfileRepository.findByAuthUserId(authUserId);

		if (profile.isEmpty()) {
			throw new ResourceNotFoundException("Profile not found.");
		}

		return profile.get();
	}

	@Override
	public UserProfile updateProfile(Long authUserId, UserProfile userProfile) {

		UserProfile existingProfile = userProfileRepository.findByAuthUserId(authUserId)
				.orElseThrow(() -> new ResourceNotFoundException("Profile not found."));

		existingProfile.setFullName(userProfile.getFullName());
		existingProfile.setMobile(userProfile.getMobile());
		existingProfile.setGender(userProfile.getGender());
		existingProfile.setDateOfBirth(userProfile.getDateOfBirth());

		return userProfileRepository.save(existingProfile);
	}

	@Override
	public void deleteProfileImage(Long authUserId) {

		UserProfile profile = userProfileRepository.findByAuthUserId(authUserId)
				.orElseThrow(() -> new ResourceNotFoundException("Profile not found."));

		profile.setProfileImage(null);

		userProfileRepository.save(profile);
	}

	@Override
	public UserProfile updateProfileImage(Long authUserId, String profileImage) {

		UserProfile profile = userProfileRepository.findByAuthUserId(authUserId)
				.orElseThrow(() -> new ResourceNotFoundException("Profile not found."));

		profile.setProfileImage(profileImage);

		return userProfileRepository.save(profile);
	}

}