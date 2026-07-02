package com.msp.service;

import com.msp.dto.request.CreateAddressRequest;
import com.msp.entity.ProviderAddress;

import java.util.List;

public interface ProviderAddressService {

    ProviderAddress createAddress(Long authUserId, CreateAddressRequest request);

    ProviderAddress updateAddress(Long authUserId, Long addressId, CreateAddressRequest request);

    ProviderAddress getAddressById(Long authUserId, Long addressId);

    List<ProviderAddress> getAllAddresses(Long authUserId);

    void deleteAddress(Long authUserId, Long addressId);
}