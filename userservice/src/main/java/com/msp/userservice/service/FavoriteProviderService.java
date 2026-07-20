package com.msp.userservice.service;

import java.util.List;

import com.msp.userservice.entity.FavoriteProvider;

public interface FavoriteProviderService {
	FavoriteProvider addFavorite(Long authUserId, Long providerId);

	List<FavoriteProvider> getFavorites(Long authUserId);

	void removeFavorite(Long authUserId, Long providerId);

}
