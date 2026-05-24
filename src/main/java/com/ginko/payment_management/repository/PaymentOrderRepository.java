package com.ginko.payment_management.repository;

import com.ginko.payment_management.entity.PaymentOrder;
import com.ginko.payment_management.entity.Provider;
import com.ginko.payment_management.enums.PaymentOrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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

    @Query("""
        SELECT COALESCE(SUM(po.amount), 0)
        FROM PaymentOrder po
        WHERE po.provider.id = :providerId
        AND po.status = com.ginko.payment_management.enums.PaymentOrderStatus.PAID
        AND po.createdAt BETWEEN :startDate AND :endDate
       """)
    BigDecimal getTotalPaidByProviderAndDateRange(
            @Param("providerId") UUID providerId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    List<PaymentOrder> findByStatusAndCreatedAtBefore(
            PaymentOrderStatus status,
            LocalDateTime date
    );

}
