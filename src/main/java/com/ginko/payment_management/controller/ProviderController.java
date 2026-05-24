package com.ginko.payment_management.controller;

import com.ginko.payment_management.dto.request.CreateProviderRequest;
import com.ginko.payment_management.dto.request.UpdateProviderRequest;
import com.ginko.payment_management.dto.request.UpdateProviderStatusRequest;
import com.ginko.payment_management.dto.response.ProviderResponse;
import com.ginko.payment_management.enums.ProviderStatus;
import com.ginko.payment_management.service.ProviderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Providers", description = "Provider management APIs")
@RestController
@RequestMapping("/api/providers")
@RequiredArgsConstructor
public class ProviderController {

    private final ProviderService providerService;

    @Operation(summary = "Create a provider")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProviderResponse create(
            @Valid @RequestBody CreateProviderRequest request
    ) {

        return providerService.create(request);
    }

    @Operation(summary = "Retrieve all providers")
    @GetMapping
    public Page<ProviderResponse> getAll(
            @RequestParam(required = false) ProviderStatus status,
            Pageable pageable
    ) {

        return providerService.getAll(status, pageable);
    }

    @Operation(summary = "Retrieve a provider by id")
    @GetMapping("/{id}")
    public ProviderResponse getById(
            @PathVariable UUID id
    ) {

        return providerService.getById(id);
    }

    @Operation(summary = "Update a provider")
    @PutMapping("/{id}")
    public ProviderResponse update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateProviderRequest request
    ) {

        return providerService.update(id, request);
    }

    @Operation(summary = "Update a provider' status")
    @PatchMapping("/{id}/status")
    public ProviderResponse updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateProviderStatusRequest request
    ) {

        return providerService.updateStatus(id, request);
    }

}
