package com.ginko.payment_management.service;

import com.ginko.payment_management.dto.request.CreatePaymentOrderRequest;
import com.ginko.payment_management.dto.request.UpdatePaymentOrderStatusRequest;
import com.ginko.payment_management.dto.response.ExpiringPaymentOrderResponse;
import com.ginko.payment_management.dto.response.ProviderPaymentReportResponse;
import com.ginko.payment_management.entity.PaymentOrder;
import com.ginko.payment_management.entity.Provider;
import com.ginko.payment_management.enums.PaymentOrderStatus;
import com.ginko.payment_management.enums.ProviderStatus;
import com.ginko.payment_management.exception.BusinessException;
import com.ginko.payment_management.repository.PaymentOrderRepository;
import com.ginko.payment_management.repository.ProviderRepository;
import com.ginko.payment_management.service.impl.PaymentOrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentOrderServiceImplTest {

    @Mock
    private PaymentOrderRepository paymentOrderRepository;

    @Mock
    private ProviderRepository providerRepository;

    @InjectMocks
    private PaymentOrderServiceImpl paymentOrderService;

    @Test
    void shouldThrowExceptionWhenProviderIsInactive() {

        UUID providerId = UUID.randomUUID();

        Provider provider = Provider.builder()
                .id(providerId)
                .businessName("Inactive Provider")
                .status(ProviderStatus.INACTIVE)
                .build();

        CreatePaymentOrderRequest request =
                new CreatePaymentOrderRequest();

        request.setProviderId(providerId);
        request.setAmount(BigDecimal.valueOf(1000));
        request.setConcept("Test payment");

        when(providerRepository.findById(providerId))
                .thenReturn(Optional.of(provider));

        assertThrows(
                BusinessException.class,
                () -> paymentOrderService.create(request)
        );
    }

    @Test
    void shouldThrowExceptionForInvalidStatusTransition() {

        UUID orderId = UUID.randomUUID();

        Provider provider = Provider.builder()
                .businessName("Provider")
                .status(ProviderStatus.ACTIVE)
                .build();

        PaymentOrder order = PaymentOrder.builder()
                .id(orderId)
                .provider(provider)
                .status(PaymentOrderStatus.PAID)
                .build();

        UpdatePaymentOrderStatusRequest request =
                new UpdatePaymentOrderStatusRequest();

        request.setStatus(PaymentOrderStatus.APPROVED);

        when(paymentOrderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        assertThrows(
                BusinessException.class,
                () -> paymentOrderService.updateStatus(orderId, request)
        );
    }

    @Test
    void shouldReturnProviderPaymentReport() {

        UUID providerId = UUID.randomUUID();

        Provider provider = Provider.builder()
                .id(providerId)
                .businessName("ACME")
                .status(ProviderStatus.ACTIVE)
                .build();

        when(providerRepository.findById(providerId))
                .thenReturn(Optional.of(provider));

        when(paymentOrderRepository
                .getTotalPaidByProviderAndDateRange(
                        any(),
                        any(),
                        any()
                ))
                .thenReturn(BigDecimal.valueOf(5000000));

        ProviderPaymentReportResponse response =
                paymentOrderService.getProviderPaymentReport(
                        providerId,
                        LocalDate.now().minusDays(30),
                        LocalDate.now()
                );

        assertEquals(
                BigDecimal.valueOf(5000000),
                response.getTotalPaid()
        );
    }

    @Test
    void shouldReturnExpiringOrders() {

        Provider provider = Provider.builder()
                .businessName("ACME")
                .build();

        PaymentOrder order = PaymentOrder.builder()
                .provider(provider)
                .amount(BigDecimal.valueOf(1000))
                .concept("Test")
                .status(PaymentOrderStatus.APPROVED)
                .createdAt(LocalDateTime.now().minusDays(28))
                .build();

        when(paymentOrderRepository
                .findByStatusAndCreatedAtBefore(
                        eq(PaymentOrderStatus.APPROVED),
                        any()
                ))
                .thenReturn(List.of(order));

        List<ExpiringPaymentOrderResponse> response =
                paymentOrderService.getExpiringOrders();

        assertFalse(response.isEmpty());
    }

}