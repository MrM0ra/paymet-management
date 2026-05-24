package com.ginko.payment_management.dto.response;

import com.ginko.payment_management.enums.ProviderStatus;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ProviderResponse {

    private UUID id;

    private String businessName;

    private String taxId;

    private String email;

    private ProviderStatus status;

}
