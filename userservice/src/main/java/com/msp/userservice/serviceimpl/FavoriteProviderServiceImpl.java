package com.msp.userservice.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.msp.userservice.entity.FavoriteProvider;
import com.msp.userservice.entity.UserProfile;
import com.msp.userservice.exception.DuplicateResourceException;
import com.msp.userservice.exception.ResourceNotFoundException;
import com.msp.userservice.repository.FavoriteProviderRepository;
import com.msp.userservice.repository.UserProfileRepository;
import com.msp.userservice.service.FavoriteProviderService;

@Service
public class FavoriteProviderServiceImpl implements FavoriteProviderService {

	@Autowired
	private FavoriteProviderRepository favoriteProviderRepository;

	@Autowired
	private UserProfileRepository userProfileRepository;

	private UserProfile getUserProfile(Long authUserId) {

		return userProfileRepository.findByAuthUserId(authUserId)
				.orElseThrow(() -> new ResourceNotFoundException("User Profile not found."));
	}

	@Override
	public FavoriteProvider addFavorite(Long authUserId, Long providerId) {

		UserProfile profile = getUserProfile(authUserId);

		if (favoriteProviderRepository.existsByUserProfileAndProviderId(profile, providerId)) {

			throw new DuplicateResourceException("Provider already added to favourites.");
		}

		FavoriteProvider favoriteProvider = new FavoriteProvider();

		favoriteProvider.setUserProfile(profile);
		favoriteProvider.setProviderId(providerId);

		return favoriteProviderRepository.save(favoriteProvider);
	}

	@Override
	public List<FavoriteProvider> getFavorites(Long authUserId) {

		UserProfile profile = getUserProfile(authUserId);

		return favoriteProviderRepository.findByUserProfile(profile);
	}

	@Override
	public void removeFavorite(Long authUserId, Long providerId) {

		UserProfile profile = getUserProfile(authUserId);

		if (!favoriteProviderRepository.existsByUserProfileAndProviderId(profile, providerId)) {

			throw new ResourceNotFoundException("Favourite provider not found.");
		}

		favoriteProviderRepository.deleteByUserProfileAndProviderId(profile, providerId);
	}

}
