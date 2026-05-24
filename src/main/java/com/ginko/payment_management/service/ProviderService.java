package com.ginko.payment_management.service;

import com.ginko.payment_management.dto.request.CreateProviderRequest;
import com.ginko.payment_management.dto.request.UpdateProviderRequest;
import com.ginko.payment_management.dto.request.UpdateProviderStatusRequest;
import com.ginko.payment_management.dto.response.ProviderResponse;
import com.ginko.payment_management.enums.ProviderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProviderService {

    ProviderResponse create(CreateProviderRequest request);

    Page<ProviderResponse> getAll(
            ProviderStatus status,
            Pageable pageable
    );

    ProviderResponse getById(UUID id);

    ProviderResponse update(
            UUID id,
            UpdateProviderRequest request
    );

    ProviderResponse updateStatus(
            UUID id,
            UpdateProviderStatusRequest request
    );

}