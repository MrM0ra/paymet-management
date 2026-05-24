package com.ginko.payment_management.entity;

import com.ginko.payment_management.enums.PaymentOrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payment_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "provider_id")
    private Provider provider;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false, length = 250)
    private String concept;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentOrderStatus status = PaymentOrderStatus.DRAFT;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

}
