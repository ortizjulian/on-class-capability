package com.on_class.capability.domain.usecase;

import com.on_class.capability.domain.exceptions.BusinessException;
import com.on_class.capability.domain.spi.IBootcampCapabilityPersistencePort;
import com.on_class.capability.domain.spi.ICapabilityPersistencePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BootcampUseCaseTest {

    @Mock
    private ICapabilityPersistencePort capabilityPersistencePort;

    @Mock
    private IBootcampCapabilityPersistencePort bootcampCapabilityPersistencePort;

    @InjectMocks
    private BootcampUseCase bootcampUseCase;

    @Test
    void linkCapabilities_WhenAllCapabilitiesExist_ShouldLinkSuccessfully() {
        Long bootcampId = 1L;
        List<Long> capabilityIds = List.of(1L, 2L, 3L);

        when(capabilityPersistencePort.existAllByIds(capabilityIds))
                .thenReturn(Mono.just(true));
        when(bootcampCapabilityPersistencePort.registerBootcampCapabilities(bootcampId, capabilityIds))
                .thenReturn(Mono.empty());

        StepVerifier.create(bootcampUseCase.linkCapabilities(bootcampId, capabilityIds))
                .verifyComplete();
    }

    @Test
    void linkCapabilities_WhenNotAllCapabilitiesExist_ShouldThrowBusinessException() {
        Long bootcampId = 1L;
        List<Long> capabilityIds = List.of(1L, 2L, 3L);

        when(capabilityPersistencePort.existAllByIds(capabilityIds))
                .thenReturn(Mono.just(false));

        StepVerifier.create(bootcampUseCase.linkCapabilities(bootcampId, capabilityIds))
                .expectError(BusinessException.class)
                .verify();
    }
}