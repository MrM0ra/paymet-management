package com.ginko.payment_management.repository;

import com.ginko.payment_management.entity.Provider;
import com.ginko.payment_management.enums.ProviderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProviderRepository extends JpaRepository<Provider, UUID> {

    boolean existsByTaxId(String taxId);

    Page<Provider> findByStatus(
            ProviderStatus status,
            Pageable pageable
    );

}
