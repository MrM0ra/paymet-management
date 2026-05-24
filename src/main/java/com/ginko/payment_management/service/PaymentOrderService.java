package com.ginko.payment_management.service;

import com.ginko.payment_management.dto.request.CreatePaymentOrderRequest;
import com.ginko.payment_management.dto.request.UpdatePaymentOrderStatusRequest;
import com.ginko.payment_management.dto.response.ExpiringPaymentOrderResponse;
import com.ginko.payment_management.dto.response.PaymentOrderResponse;
import com.ginko.payment_management.dto.response.ProviderPaymentReportResponse;
import com.ginko.payment_management.enums.PaymentOrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface PaymentOrderService {

    PaymentOrderResponse create(CreatePaymentOrderRequest request);

    Page<PaymentOrderResponse> getAll(
            PaymentOrderStatus status,
            UUID providerId,
            Pageable pageable
    );

    PaymentOrderResponse getById(UUID id);

    PaymentOrderResponse updateStatus(
            UUID id,
            UpdatePaymentOrderStatusRequest request
    );

    ProviderPaymentReportResponse getProviderPaymentReport(
            UUID providerId,
            LocalDate startDate,
            LocalDate endDate
    );

    List<ExpiringPaymentOrderResponse> getExpiringOrders();

}