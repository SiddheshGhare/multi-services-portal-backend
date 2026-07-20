package com.msp.userservice.service;

import com.msp.userservice.entity.UserProfile;

public interface UserProfileService {
	UserProfile createProfile(Long authUserId, UserProfile userProfile);

	UserProfile getProfile(Long authUserId);

	UserProfile updateProfile(Long authUserId, UserProfile userProfile);

	void deleteProfileImage(Long authUserId);

	UserProfile updateProfileImage(Long authUserId, String profileImage);

}
