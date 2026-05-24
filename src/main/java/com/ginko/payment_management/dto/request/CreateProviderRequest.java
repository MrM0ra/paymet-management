package com.ginko.payment_management.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateProviderRequest {

    @NotBlank(message = "Business name is required")
    private String businessName;

    @NotBlank(message = "Tax ID is required")
    private String taxId;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

}
