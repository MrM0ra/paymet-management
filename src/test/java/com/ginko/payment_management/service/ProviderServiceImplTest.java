package com.ginko.payment_management.service;

import com.ginko.payment_management.dto.request.CreateProviderRequest;
import com.ginko.payment_management.exception.BusinessException;
import com.ginko.payment_management.repository.ProviderRepository;
import com.ginko.payment_management.service.impl.ProviderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProviderServiceImplTest {

    @Mock
    private ProviderRepository providerRepository;

    @InjectMocks
    private ProviderServiceImpl providerService;

    @Test
    void shouldThrowExceptionWhenTaxIdAlreadyExists() {

        CreateProviderRequest request = new CreateProviderRequest();
        request.setBusinessName("Test Provider");
        request.setTaxId("123456");
        request.setEmail("test@test.com");

        when(providerRepository.existsByTaxId("123456"))
                .thenReturn(true);

        assertThrows(
                BusinessException.class,
                () -> providerService.create(request)
        );
    }

}