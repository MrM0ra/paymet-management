package com.ginko.payment_management.service.impl;

import com.ginko.payment_management.dto.request.CreatePaymentOrderRequest;
import com.ginko.payment_management.dto.request.UpdatePaymentOrderStatusRequest;
import com.ginko.payment_management.dto.response.ExpiringPaymentOrderResponse;
import com.ginko.payment_management.dto.response.PaymentOrderResponse;
import com.ginko.payment_management.dto.response.ProviderPaymentReportResponse;
import com.ginko.payment_management.entity.PaymentOrder;
import com.ginko.payment_management.entity.Provider;
import com.ginko.payment_management.enums.PaymentOrderStatus;
import com.ginko.payment_management.enums.ProviderStatus;
import com.ginko.payment_management.exception.BusinessException;
import com.ginko.payment_management.exception.ResourceNotFoundException;
import com.ginko.payment_management.repository.PaymentOrderRepository;
import com.ginko.payment_management.repository.ProviderRepository;
import com.ginko.payment_management.service.PaymentOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentOrderServiceImpl implements PaymentOrderService {

    private final PaymentOrderRepository paymentOrderRepository;
    private final ProviderRepository providerRepository;

    @Override
    public PaymentOrderResponse create(CreatePaymentOrderRequest request) {

        Provider provider = providerRepository.findById(request.getProviderId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Provider not found"));

        if (provider.getStatus() != ProviderStatus.ACTIVE) {
            throw new BusinessException("Provider must be ACTIVE");
        }

        PaymentOrder paymentOrder = PaymentOrder.builder()
                .provider(provider)
                .amount(request.getAmount())
                .concept(request.getConcept())
                .status(PaymentOrderStatus.DRAFT)
                .build();

        PaymentOrder savedOrder = paymentOrderRepository.save(paymentOrder);

        return mapToResponse(savedOrder);
    }

    @Override
    public Page<PaymentOrderResponse> getAll(
            PaymentOrderStatus status,
            UUID providerId,
            Pageable pageable
    ) {

        Page<PaymentOrder> orders;

        if (status != null && providerId != null) {

            Provider provider = findProviderById(providerId);

            orders = paymentOrderRepository.findByStatusAndProvider(
                    status,
                    provider,
                    pageable
            );

        } else if (status != null) {

            orders = paymentOrderRepository.findByStatus(
                    status,
                    pageable
            );

        } else if (providerId != null) {

            Provider provider = findProviderById(providerId);

            orders = paymentOrderRepository.findByProvider(
                    provider,
                    pageable
            );

        } else {

            orders = paymentOrderRepository.findAll(pageable);
        }

        return orders.map(this::mapToResponse);
    }

    @Override
    public PaymentOrderResponse getById(UUID id) {

        PaymentOrder order = findOrderById(id);

        return mapToResponse(order);
    }

    @Override
    public PaymentOrderResponse updateStatus(
            UUID id,
            UpdatePaymentOrderStatusRequest request
    ) {

        PaymentOrder order = findOrderById(id);

        if (!isValidTransition(order.getStatus(), request.getStatus())) {
            throw new BusinessException(
                    "Invalid status transition from "
                            + order.getStatus()
                            + " to "
                            + request.getStatus()
            );
        }

        order.setStatus(request.getStatus());

        PaymentOrder updatedOrder = paymentOrderRepository.save(order);

        return mapToResponse(updatedOrder);
    }

    private boolean isValidTransition(
            PaymentOrderStatus current,
            PaymentOrderStatus next
    ) {

        return switch (current) {

            case DRAFT ->
                    next == PaymentOrderStatus.APPROVED
                            || next == PaymentOrderStatus.REJECTED;

            case APPROVED ->
                    next == PaymentOrderStatus.PAID;

            default -> false;
        };
    }

    private PaymentOrder findOrderById(UUID id) {

        return paymentOrderRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Payment order not found"));
    }

    private Provider findProviderById(UUID id) {

        return providerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Provider not found"));
    }

    private PaymentOrderResponse mapToResponse(PaymentOrder order) {

        return PaymentOrderResponse.builder()
                .id(order.getId())
                .providerId(order.getProvider().getId())
                .providerName(order.getProvider().getBusinessName())
                .amount(order.getAmount())
                .concept(order.getConcept())
                .createdAt(order.getCreatedAt())
                .status(order.getStatus())
                .build();
    }

    @Override
    public ProviderPaymentReportResponse getProviderPaymentReport(
            UUID providerId,
            LocalDate startDate,
            LocalDate endDate
    ) {

        Provider provider = findProviderById(providerId);

        BigDecimal totalPaid =
                paymentOrderRepository.getTotalPaidByProviderAndDateRange(
                        providerId,
                        startDate.atStartOfDay(),
                        endDate.atTime(23, 59, 59)
                );

        return ProviderPaymentReportResponse.builder()
                .providerId(provider.getId())
                .providerName(provider.getBusinessName())
                .startDate(startDate)
                .endDate(endDate)
                .totalPaid(totalPaid)
                .build();
    }

    @Override
    public List<ExpiringPaymentOrderResponse> getExpiringOrders() {

        LocalDateTime threshold =
                LocalDateTime.now().minusDays(25);

        List<PaymentOrder> orders =
                paymentOrderRepository.findByStatusAndCreatedAtBefore(
                        PaymentOrderStatus.APPROVED,
                        threshold
                );

        return orders.stream()
                .map(order -> ExpiringPaymentOrderResponse.builder()
                        .orderId(order.getId())
                        .providerName(order.getProvider().getBusinessName())
                        .amount(order.getAmount())
                        .concept(order.getConcept())
                        .createdAt(order.getCreatedAt())
                        .daysPending(
                                ChronoUnit.DAYS.between(
                                        order.getCreatedAt(),
                                        LocalDateTime.now()
                                )
                        )
                        .build())
                .toList();
    }

}
