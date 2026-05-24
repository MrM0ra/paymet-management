package com.ginko.payment_management.dto.request;

import com.ginko.payment_management.enums.PaymentOrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdatePaymentOrderStatusRequest {

    @NotNull(message = "Status is required")
    private PaymentOrderStatus status;

}
