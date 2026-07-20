package com.msp.userservice.serviceimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.msp.userservice.entity.FavoriteProvider;
import com.msp.userservice.entity.UserAddress;
import com.msp.userservice.entity.UserProfile;
import com.msp.userservice.exception.ResourceNotFoundException;
import com.msp.userservice.repository.FavoriteProviderRepository;
import com.msp.userservice.repository.UserAddressRepository;
import com.msp.userservice.repository.UserProfileRepository;
import com.msp.userservice.service.DashboardService;

@Service
public class DashboardServiceImpl implements DashboardService {

	@Autowired
	private UserProfileRepository userProfileRepository;

	@Autowired
	private UserAddressRepository userAddressRepository;

	@Autowired
	private FavoriteProviderRepository favoriteProviderRepository;

	@Override
	public Map<String, Object> getDashboard(Long authUserId) {

		UserProfile profile = userProfileRepository.findByAuthUserId(authUserId)
				.orElseThrow(() -> new ResourceNotFoundException("User profile not found."));

		List<UserAddress> addresses = userAddressRepository.findByUserProfile(profile);

		List<FavoriteProvider> favorites = favoriteProviderRepository.findByUserProfile(profile);

		Map<String, Object> dashboard = new HashMap<>();

		dashboard.put("profile", profile);
		dashboard.put("addresses", addresses);
		dashboard.put("favorites", favorites);

		return dashboard;
	}

}
