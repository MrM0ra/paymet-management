package com.ginko.payment_management.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class ProviderPaymentReportResponse {

    private UUID providerId;

    private String providerName;

    private LocalDate startDate;

    private LocalDate endDate;

    private BigDecimal totalPaid;

}
