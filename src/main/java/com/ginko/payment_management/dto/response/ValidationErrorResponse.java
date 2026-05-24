package com.ginko.payment_management.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class ValidationErrorResponse {

    private LocalDateTime timestamp;

    private Integer status;

    private String error;

    private Map<String, String> validations;

}
