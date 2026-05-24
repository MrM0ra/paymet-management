package com.ginko.payment_management.dto.request;

import com.ginko.payment_management.enums.ProviderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateProviderStatusRequest {
    @NotNull(message = "Status is required")
    private ProviderStatus status;
}
