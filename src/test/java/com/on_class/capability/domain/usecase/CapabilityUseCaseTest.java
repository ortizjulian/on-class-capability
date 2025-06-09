package com.on_class.capability.domain.usecase;

import com.on_class.capability.domain.exceptions.BusinessException;
import com.on_class.capability.domain.model.Capability;
import com.on_class.capability.domain.model.PaginationAndFilter;
import com.on_class.capability.domain.model.PaginationResponse;
import com.on_class.capability.domain.model.Technology;
import com.on_class.capability.domain.spi.ICapabilityPersistencePort;
import com.on_class.capability.domain.spi.ITechnologyExternalPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
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

        StepVerifier.create(capabilityUseCase.createCapability(capability))
                .verifyComplete();

        verify(capabilityPersistencePort).findByName("DevOps");
        verify(capabilityPersistencePort).createCapability(capability);
        verify(technologyExternalPort).linkTechnologiesToCapability(1L, capability.getTechnologies());
    }

    @Test
    void getCapabilities_whenInvalidPaginationParameters_thenThrowsBusinessException() {
        PaginationAndFilter invalidPagination = new PaginationAndFilter(-1, 0, "up", "description");

        StepVerifier.create(capabilityUseCase.getCapabilities(invalidPagination))
                .expectError(BusinessException.class)
                .verify();

        verifyNoInteractions(capabilityPersistencePort, technologyExternalPort);
    }

    @Test
    void getCapabilities_whenValidRequest_thenSuccessWithEnrichedCapabilities() {
        PaginationAndFilter validPagination = new PaginationAndFilter(1, 10, "asc", "name");
        List<Capability> capabilities = List.of(
            new Capability(1L, "DevOps", "Description 1", List.of()),
            new Capability(2L, "Backend", "Description 2", List.of())
        );
        PaginationResponse<Capability> paginationResponse = new PaginationResponse<>(1, 1, 2L, capabilities);

        List<Capability> enrichedCapabilities = List.of(
            new Capability(1L, "DevOps", "Description 1", 
                List.of(new Technology(1L, "Docker"), new Technology(2L, "Kubernetes"))),
            new Capability(2L, "Backend", "Description 2",
                List.of(new Technology(3L, "Java"), new Technology(4L, "Spring")))
        );

        when(capabilityPersistencePort.getCapabilities(validPagination))
            .thenReturn(Mono.just(paginationResponse));

        when(technologyExternalPort.getTecnologiesByCapabilities(List.of(1L, 2L)))
            .thenReturn(Flux.fromIterable(enrichedCapabilities));

        StepVerifier.create(capabilityUseCase.getCapabilities(validPagination))
            .expectNextMatches(response -> 
                response.getTotalElements() == 2L &&
                response.getElements().size() == 2 &&
                response.getElements().get(0).getTechnologies().size() == 2 &&
                response.getElements().get(1).getTechnologies().size() == 2)
            .verifyComplete();

        verify(capabilityPersistencePort).getCapabilities(validPagination);
        verify(technologyExternalPort).getTecnologiesByCapabilities(List.of(1L, 2L));
    }

    @Test
    void getCapabilities_whenNoCapabilitiesFound_thenReturnEmptyPagination() {
        PaginationAndFilter validPagination = new PaginationAndFilter(1, 10, "asc", "name");
        PaginationResponse<Capability> emptyResponse = new PaginationResponse<>(0, 1, 0L, List.of());

        when(capabilityPersistencePort.getCapabilities(validPagination))
            .thenReturn(Mono.just(emptyResponse));

        StepVerifier.create(capabilityUseCase.getCapabilities(validPagination))
            .expectNextMatches(response -> 
                response.getTotalElements() == 0L &&
                response.getElements().isEmpty())
            .verifyComplete();

        verify(capabilityPersistencePort).getCapabilities(validPagination);
        verifyNoInteractions(technologyExternalPort);
    }
}