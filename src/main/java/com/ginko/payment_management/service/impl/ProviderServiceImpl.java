package com.ginko.payment_management.service.impl;

import com.ginko.payment_management.dto.request.CreateProviderRequest;
import com.ginko.payment_management.dto.request.UpdateProviderRequest;
import com.ginko.payment_management.dto.request.UpdateProviderStatusRequest;
import com.ginko.payment_management.dto.response.ProviderResponse;
import com.ginko.payment_management.entity.Provider;
import com.ginko.payment_management.exception.BusinessException;
import com.ginko.payment_management.exception.ResourceNotFoundException;
import com.ginko.payment_management.enums.ProviderStatus;
import com.ginko.payment_management.repository.ProviderRepository;
import com.ginko.payment_management.service.ProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProviderServiceImpl implements ProviderService {

    private final ProviderRepository providerRepository;

    @Override
    public ProviderResponse create(CreateProviderRequest request) {

        if (providerRepository.existsByTaxId(request.getTaxId())) {
            throw new BusinessException("Tax ID already exists");
        }

        Provider provider = Provider.builder()
                .businessName(request.getBusinessName())
                .taxId(request.getTaxId())
                .email(request.getEmail())
                .status(ProviderStatus.ACTIVE)
                .build();

        Provider savedProvider = providerRepository.save(provider);

        return mapToResponse(savedProvider);
    }

    @Override
    public Page<ProviderResponse> getAll(
            ProviderStatus status,
            Pageable pageable
    ) {

        Page<Provider> providers;

        if (status != null) {
            providers = providerRepository.findByStatus(status, pageable);
        } else {
            providers = providerRepository.findAll(pageable);
        }

        return providers.map(this::mapToResponse);
    }

    @Override
    public ProviderResponse getById(UUID id) {

        Provider provider = findProviderById(id);

        return mapToResponse(provider);
    }

    @Override
    public ProviderResponse update(
            UUID id,
            UpdateProviderRequest request
    ) {

        Provider provider = findProviderById(id);

        if (!provider.getTaxId().equals(request.getTaxId())
                && providerRepository.existsByTaxId(request.getTaxId())) {

            throw new BusinessException("Tax ID already exists");
        }

        provider.setBusinessName(request.getBusinessName());
        provider.setTaxId(request.getTaxId());
        provider.setEmail(request.getEmail());

        Provider updatedProvider = providerRepository.save(provider);

        return mapToResponse(updatedProvider);
    }

    @Override
    public ProviderResponse updateStatus(
            UUID id,
            UpdateProviderStatusRequest request
    ) {

        Provider provider = findProviderById(id);

        provider.setStatus(request.getStatus());

        Provider updatedProvider = providerRepository.save(provider);

        return mapToResponse(updatedProvider);
    }

    private Provider findProviderById(UUID id) {

        return providerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Provider not found"));
    }

    private ProviderResponse mapToResponse(Provider provider) {

        return ProviderResponse.builder()
                .id(provider.getId())
                .businessName(provider.getBusinessName())
                .taxId(provider.getTaxId())
                .email(provider.getEmail())
                .status(provider.getStatus())
                .build();
    }

}