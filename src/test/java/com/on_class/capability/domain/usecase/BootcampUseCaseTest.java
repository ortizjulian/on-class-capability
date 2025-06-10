package com.on_class.capability.domain.usecase;

import com.on_class.capability.domain.exceptions.BusinessException;
import com.on_class.capability.domain.model.BootcampCapability;
import com.on_class.capability.domain.model.Capability;
import com.on_class.capability.domain.model.Technology;
import com.on_class.capability.domain.spi.IBootcampCapabilityPersistencePort;
import com.on_class.capability.domain.spi.ICapabilityPersistencePort;
import com.on_class.capability.domain.spi.ITechnologyExternalPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BootcampUseCaseTest {

    @Mock
    private ICapabilityPersistencePort capabilityPersistencePort;

    @Mock
    private IBootcampCapabilityPersistencePort bootcampCapabilityPersistencePort;
    
    @Mock
    private ITechnologyExternalPort technologyExternalPort;

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
    
    @Test
    void getBootcampsCapabilitiesByIds_WhenBootcampsExist_ShouldReturnBootcampsWithCapabilities() {
        // Arrange
        List<Long> bootcampIds = List.of(1L, 2L);
        
        // Bootcamp 1 capabilities
        BootcampCapability bootcampCapability1 = new BootcampCapability(1L, 10L);
        BootcampCapability bootcampCapability2 = new BootcampCapability(1L, 11L);
        
        // Bootcamp 2 capabilities
        BootcampCapability bootcampCapability3 = new BootcampCapability(2L, 12L);
        
        // Capabilities from DB for bootcamp 1
        Capability capability1 = new Capability(10L, "Java", "Java programming", List.of());
        Capability capability2 = new Capability(11L, "Spring", "Spring framework", List.of());
        
        // Capabilities from DB for bootcamp 2
        Capability capability3 = new Capability(12L, "React", "React framework", List.of());
        
        // Technologies for capabilities
        List<Technology> techsForCapability1 = List.of(
                new Technology(100L, "Java 17"),
                new Technology(101L, "Maven")
        );
        
        List<Technology> techsForCapability2 = List.of(
                new Technology(102L, "Spring Boot"),
                new Technology(103L, "Spring WebFlux")
        );
        
        List<Technology> techsForCapability3 = List.of(
                new Technology(104L, "React JS"),
                new Technology(105L, "Redux")
        );
        
        // Capabilities with technologies for bootcamp 1
        Capability capabilityWithTech1 = new Capability(10L, "Java", "Java programming", techsForCapability1);
        Capability capabilityWithTech2 = new Capability(11L, "Spring", "Spring framework", techsForCapability2);
        
        // Capabilities with technologies for bootcamp 2
        Capability capabilityWithTech3 = new Capability(12L, "React", "React framework", techsForCapability3);
        
        // Mock bootcampCapabilityPersistencePort.getCapabilitiesByBootcampIds
        when(bootcampCapabilityPersistencePort.getCapabilitiesByBootcampIds(bootcampIds))
                .thenReturn(Flux.just(bootcampCapability1, bootcampCapability2, bootcampCapability3));
        
        // Mock capabilityPersistencePort.findByIds for bootcamp 1
        when(capabilityPersistencePort.findByIds(List.of(10L, 11L)))
                .thenReturn(Flux.just(capability1, capability2));
        
        // Mock capabilityPersistencePort.findByIds for bootcamp 2
        when(capabilityPersistencePort.findByIds(List.of(12L)))
                .thenReturn(Flux.just(capability3));
        
        // Mock technologyExternalPort.getTecnologiesByCapabilities for bootcamp 1
        when(technologyExternalPort.getTechnologiesByCapabilities(List.of(10L, 11L)))
                .thenReturn(Flux.just(capabilityWithTech1, capabilityWithTech2));
        
        // Mock technologyExternalPort.getTecnologiesByCapabilities for bootcamp 2
        when(technologyExternalPort.getTechnologiesByCapabilities(List.of(12L)))
                .thenReturn(Flux.just(capabilityWithTech3));
        
        // Act & Assert
        StepVerifier.create(bootcampUseCase.getBootcampsCapabilitiesByIds(bootcampIds))
                .expectNextMatches(bootcamp -> 
                    bootcamp.getId().equals(1L) && 
                    bootcamp.getCapabilities().size() == 2 &&
                    bootcamp.getCapabilities().get(0).getTechnologies().size() == 2 &&
                    bootcamp.getCapabilities().get(1).getTechnologies().size() == 2)
                .expectNextMatches(bootcamp -> 
                    bootcamp.getId().equals(2L) && 
                    bootcamp.getCapabilities().size() == 1 &&
                    bootcamp.getCapabilities().get(0).getTechnologies().size() == 2)
                .verifyComplete();
        
        // Verify interactions
        verify(bootcampCapabilityPersistencePort).getCapabilitiesByBootcampIds(bootcampIds);
        verify(capabilityPersistencePort).findByIds(List.of(10L, 11L));
        verify(capabilityPersistencePort).findByIds(List.of(12L));
        verify(technologyExternalPort).getTechnologiesByCapabilities(List.of(10L, 11L));
        verify(technologyExternalPort).getTechnologiesByCapabilities(List.of(12L));
    }
    
    @Test
    void getBootcampsCapabilitiesByIds_WhenNoBootcampsExist_ShouldReturnEmptyFlux() {
        // Arrange
        List<Long> bootcampIds = List.of(1L, 2L);
        
        // Mock bootcampCapabilityPersistencePort.getCapabilitiesByBootcampIds to return empty
        when(bootcampCapabilityPersistencePort.getCapabilitiesByBootcampIds(bootcampIds))
                .thenReturn(Flux.empty());
        
        // Act & Assert
        StepVerifier.create(bootcampUseCase.getBootcampsCapabilitiesByIds(bootcampIds))
                .verifyComplete();
        
        // Verify interactions
        verify(bootcampCapabilityPersistencePort).getCapabilitiesByBootcampIds(bootcampIds);
        verifyNoMoreInteractions(capabilityPersistencePort, technologyExternalPort);
    }
    
    @Test
    void getBootcampsCapabilitiesByIds_WhenCapabilitiesExistButNoTechnologies_ShouldReturnBootcampsWithEmptyTechnologies() {
        // Arrange
        List<Long> bootcampIds = List.of(1L);
        
        // Bootcamp 1 capabilities
        BootcampCapability bootcampCapability1 = new BootcampCapability(1L, 10L);
        
        // Capabilities from DB for bootcamp 1
        Capability capability1 = new Capability(10L, "Java", "Java programming", List.of());
        
        // Mock bootcampCapabilityPersistencePort.getCapabilitiesByBootcampIds
        when(bootcampCapabilityPersistencePort.getCapabilitiesByBootcampIds(bootcampIds))
                .thenReturn(Flux.just(bootcampCapability1));
        
        // Mock capabilityPersistencePort.findByIds
        when(capabilityPersistencePort.findByIds(List.of(10L)))
                .thenReturn(Flux.just(capability1));
        
        // Mock technologyExternalPort.getTecnologiesByCapabilities to return empty technologies
        when(technologyExternalPort.getTechnologiesByCapabilities(List.of(10L)))
                .thenReturn(Flux.empty());
        
        // Act & Assert
        StepVerifier.create(bootcampUseCase.getBootcampsCapabilitiesByIds(bootcampIds))
                .expectNextMatches(bootcamp -> 
                    bootcamp.getId().equals(1L) && 
                    bootcamp.getCapabilities().size() == 1 &&
                    bootcamp.getCapabilities().get(0).getTechnologies().isEmpty())
                .verifyComplete();
        
        // Verify interactions
        verify(bootcampCapabilityPersistencePort).getCapabilitiesByBootcampIds(bootcampIds);
        verify(capabilityPersistencePort).findByIds(List.of(10L));
        verify(technologyExternalPort).getTechnologiesByCapabilities(List.of(10L));
    }
}