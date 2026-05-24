package com.ginko.payment_management.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ExpiringPaymentOrderResponse {

    private UUID orderId;

    private String providerName;

    private BigDecimal amount;

    private String concept;

    private LocalDateTime createdAt;

    private Long daysPending;

}