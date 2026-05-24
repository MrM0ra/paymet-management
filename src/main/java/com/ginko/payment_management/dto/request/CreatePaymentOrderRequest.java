package com.ginko.payment_management.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreatePaymentOrderRequest {

    @NotNull(message = "Provider ID is required")
    private UUID providerId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotBlank(message = "Concept is required")
    @Size(max = 250, message = "Concept cannot exceed 250 characters")
    private String concept;

}
