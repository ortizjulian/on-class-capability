package com.on_class.capability.domain.usecase;

import com.on_class.capability.domain.exceptions.BusinessException;
import com.on_class.capability.domain.model.Capability;
import com.on_class.capability.domain.model.Technology;
import com.on_class.capability.domain.spi.ICapabilityPersistencePort;
import com.on_class.capability.domain.spi.ITechnologyExternalPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CapabilityUseCaseTest {

    @Mock
    private ICapabilityPersistencePort capabilityPersistencePort;

    @Mock
    private ITechnologyExternalPort technologyExternalPort;

    @InjectMocks
    private CapabilityUseCase capabilityUseCase;

    @Test
    void createCapability_whenNameAlreadyExists_thenThrowsBusinessException(){

        Capability capability = new Capability(null, "DevOps", "Saber mucho Devops",
                List.of(new Technology(1L, "Docker"), new Technology(2L, "Kubernetes"),new Technology(3L, "Azure")));

        when(capabilityPersistencePort.findByName(capability.getName())).thenReturn(Mono.just(capability));

        StepVerifier.create(capabilityUseCase.createCapability(capability))
                .expectError(BusinessException.class)
                .verify();

        verify(capabilityPersistencePort).findByName("DevOps");
        verify(capabilityPersistencePort, Mockito.never()).createCapability(Mockito.any());
    }

    @Test
    void createCapability_WhenInvalidTechnologyList_thenThrowsBusinessException() {
        Capability capability = new Capability(1L, "DevOps","Saber mucho Devops" , List.of());

        StepVerifier.create(capabilityUseCase.createCapability(capability))
                .expectError(BusinessException.class)
                .verify();

        verifyNoInteractions(capabilityPersistencePort, technologyExternalPort);
    }

    @Test
    void createCapability_whenNameDoesNotExistsButLinkTechnologiesFails_thenThrowsBusinessException() {
        Capability capability = new Capability(null, "DevOps", "Saber mucho Devops",
                List.of(new Technology(1L, "Docker"), new Technology(2L, "Kubernetes"),new Technology(3L, "Azure")));

        when(capabilityPersistencePort.findByName("DevOps"))
                .thenReturn(Mono.empty());

        when(capabilityPersistencePort.createCapability(capability))
                .thenReturn(Mono.just(new Capability(1L, "DevOps","Saber mucho Devops", capability.getTechnologies())));

        when(technologyExternalPort.linkTechnologiesToCapability(1L, capability.getTechnologies()))
                .thenReturn(Mono.error(new RuntimeException("Linking failed")));

        when(capabilityPersistencePort.deleteCapabilityById(1L))
                .thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(capabilityUseCase.createCapability(capability))
                .expectErrorMatches(ex -> ex instanceof RuntimeException &&
                        ex.getMessage().equals("Linking failed"))
                .verify();

        verify(capabilityPersistencePort).findByName("DevOps");
        verify(capabilityPersistencePort).createCapability(capability);
        verify(technologyExternalPort).linkTechnologiesToCapability(1L, capability.getTechnologies());
        verify(capabilityPersistencePort).deleteCapabilityById(1L);
    }

    @Test
    void createCapability_WhenEverythingIsValid_ThenSuccess() {
        Capability capability = new Capability(null, "DevOps", "Saber mucho Devops",
                List.of(new Technology(1L, "Docker"), new Technology(2L, "Kubernetes"),new Technology(3L, "Azure")));

        when(capabilityPersistencePort.findByName("DevOps"))
                .thenReturn(Mono.empty());

        when(capabilityPersistencePort.createCapability(capability))
                .thenReturn(Mono.just(new Capability(1L, "DevOps","Saber mucho Devops", capability.getTechnologies())));

        when(technologyExternalPort.linkTechnologiesToCapability(1L, capability.getTechnologies()))
                .thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(capabilityUseCase.createCapability(capability))
                .verifyComplete();

        verify(capabilityPersistencePort).findByName("DevOps");
        verify(capabilityPersistencePort).createCapability(capability);
        verify(technologyExternalPort).linkTechnologiesToCapability(1L, capability.getTechnologies());
    }
}