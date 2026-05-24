package com.ginko.payment_management.controller;

import com.ginko.payment_management.dto.request.CreatePaymentOrderRequest;
import com.ginko.payment_management.dto.request.UpdatePaymentOrderStatusRequest;
import com.ginko.payment_management.dto.response.ExpiringPaymentOrderResponse;
import com.ginko.payment_management.dto.response.PaymentOrderResponse;
import com.ginko.payment_management.dto.response.ProviderPaymentReportResponse;
import com.ginko.payment_management.enums.PaymentOrderStatus;
import com.ginko.payment_management.service.PaymentOrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Tag(name = "Payment Orders", description = "Payment Orders management APIs")
@RestController
@RequestMapping("/api/payment-orders")
@RequiredArgsConstructor
public class PaymentOrderController {

    private final PaymentOrderService paymentOrderService;

    @Operation(summary = "Create a payment order")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentOrderResponse create(
            @Valid @RequestBody CreatePaymentOrderRequest request
    ) {

        return paymentOrderService.create(request);
    }

    @Operation(summary = "Retrieve all payment orders")
    @GetMapping
    public Page<PaymentOrderResponse> getAll(
            @RequestParam(required = false) PaymentOrderStatus status,
            @RequestParam(required = false) UUID providerId,
            Pageable pageable
    ) {

        return paymentOrderService.getAll(
                status,
                providerId,
                pageable
        );
    }

    @Operation(summary = "Retrieve a payment order by id")
    @GetMapping("/{id}")
    public PaymentOrderResponse getById(
            @PathVariable UUID id
    ) {

        return paymentOrderService.getById(id);
    }

    @Operation(summary = "Update a payment order's status")
    @PatchMapping("/{id}/status")
    public PaymentOrderResponse updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePaymentOrderStatusRequest request
    ) {

        return paymentOrderService.updateStatus(id, request);
    }

    @Operation(summary = "Get provider's total paid within a date range")
    @GetMapping("/reports/provider-payments")
    public ProviderPaymentReportResponse getProviderPaymentReport(
            @RequestParam UUID providerId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {

        return paymentOrderService.getProviderPaymentReport(
                providerId,
                startDate,
                endDate
        );
    }

    @Operation(summary = "Get soon to expire orders")
    @GetMapping("/expiring")
    public List<ExpiringPaymentOrderResponse> getExpiringOrders() {

        return paymentOrderService.getExpiringOrders();
    }

}
