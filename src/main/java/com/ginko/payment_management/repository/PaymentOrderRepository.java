package com.ginko.payment_management.repository;

import com.ginko.payment_management.entity.PaymentOrder;
import com.ginko.payment_management.entity.Provider;
import com.ginko.payment_management.enums.PaymentOrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, UUID> {

    Page<PaymentOrder> findByStatus(
            PaymentOrderStatus status,
            Pageable pageable
    );

    Page<PaymentOrder> findByProvider(
            Provider provider,
            Pageable pageable
    );

    Page<PaymentOrder> findByStatusAndProvider(
            PaymentOrderStatus status,
            Provider provider,
            Pageable pageable
    );

}
