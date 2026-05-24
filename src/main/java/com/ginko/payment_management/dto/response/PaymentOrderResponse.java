package com.ginko.payment_management.dto.response;

import com.ginko.payment_management.enums.PaymentOrderStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class PaymentOrderResponse {

    private UUID id;

    private UUID providerId;

    private String providerName;

    private BigDecimal amount;

    private String concept;

    private LocalDateTime createdAt;

    private PaymentOrderStatus status;

}
