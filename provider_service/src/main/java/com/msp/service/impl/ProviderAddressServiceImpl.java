package com.msp.service.impl;

import com.msp.dto.request.CreateAddressRequest;
import com.msp.entity.ProviderAddress;
import com.msp.entity.ProviderProfile;
import com.msp.repository.ProviderAddressRepository;
import com.msp.repository.ProviderProfileRepository;
import com.msp.service.ProviderAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProviderAddressServiceImpl implements ProviderAddressService {

    private final ProviderAddressRepository addressRepository;
    private final ProviderProfileRepository profileRepository;

    @Override
    @Transactional
    public ProviderAddress createAddress(Long authUserId, CreateAddressRequest request) {

        ProviderProfile provider = getProvider(authUserId);

        // If this is first address OR marked primary → reset others
        if (Boolean.TRUE.equals(request.getPrimaryAddress())) {
            unsetExistingPrimary(provider);
        }

        ProviderAddress address = ProviderAddress.builder()
                .provider(provider)
                .addressLine(request.getAddressLine())
                .city(request.getCity())
                .state(request.getState())
                .pincode(request.getPincode())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .primaryAddress(Boolean.TRUE.equals(request.getPrimaryAddress()))
                .build();

        return addressRepository.save(address);
    }

    @Override
    @Transactional
    public ProviderAddress updateAddress(Long authUserId, Long addressId, CreateAddressRequest request) {

        ProviderProfile provider = getProvider(authUserId);

        ProviderAddress address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        validateOwnership(address, provider);

        address.setAddressLine(request.getAddressLine());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPincode(request.getPincode());
        address.setLatitude(request.getLatitude());
        address.setLongitude(request.getLongitude());

        if (Boolean.TRUE.equals(request.getPrimaryAddress())) {
            unsetExistingPrimary(provider);
            address.setPrimaryAddress(true);
        }

        return addressRepository.save(address);
    }

    @Override
    public ProviderAddress getAddressById(Long authUserId, Long addressId) {

        ProviderProfile provider = getProvider(authUserId);

        ProviderAddress address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        validateOwnership(address, provider);

        return address;
    }

    @Override
    public List<ProviderAddress> getAllAddresses(Long authUserId) {

        ProviderProfile provider = getProvider(authUserId);

        return addressRepository.findByProviderId(provider.getId());
    }

    @Override
    public void deleteAddress(Long authUserId, Long addressId) {

        ProviderProfile provider = getProvider(authUserId);

        ProviderAddress address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        validateOwnership(address, provider);

        addressRepository.delete(address);
    }

    // ----------------- PRIVATE HELPERS -----------------

    private ProviderProfile getProvider(Long authUserId) {
        return profileRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new RuntimeException("Provider profile not found"));
    }

    private void validateOwnership(ProviderAddress address, ProviderProfile provider) {
        if (!address.getProvider().getId().equals(provider.getId())) {
            throw new RuntimeException("Unauthorized access");
        }
    }

    private void unsetExistingPrimary(ProviderProfile provider) {
        List<ProviderAddress> addresses = addressRepository.findByProviderId(provider.getId());

        for (ProviderAddress addr : addresses) {
            addr.setPrimaryAddress(false);
        }

        addressRepository.saveAll(addresses);
    }
}