package com.msp.userservice.service;

import java.util.List;

import com.msp.userservice.entity.UserAddress;

public interface UserAddressService {
	UserAddress addAddress(Long authUserId, UserAddress address);

	List<UserAddress> getAllAddresses(Long authUserId);

	UserAddress getAddressById(Long authUserId, Long addressId);

	UserAddress updateAddress(Long authUserId, Long addressId, UserAddress address);

	void deleteAddress(Long authUserId, Long addressId);

	UserAddress makeDefaultAddress(Long authUserId, Long addressId);

}
