package com.on_class.capability.domain.usecase;

import com.on_class.capability.domain.api.IBootcampServicePort;
import com.on_class.capability.domain.enums.TechnicalMessage;
import com.on_class.capability.domain.exceptions.BusinessException;
import com.on_class.capability.domain.model.Bootcamp;
import com.on_class.capability.domain.model.BootcampCapability;
import com.on_class.capability.domain.model.Capability;
import com.on_class.capability.domain.model.Technology;
import com.on_class.capability.domain.spi.IBootcampCapabilityPersistencePort;
import com.on_class.capability.domain.spi.ICapabilityPersistencePort;
import com.on_class.capability.domain.spi.ITechnologyExternalPort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

public class BootcampUseCase implements IBootcampServicePort {

    private final ICapabilityPersistencePort capabilityPersistencePort;
    private final IBootcampCapabilityPersistencePort bootcampCapabilityPersistencePort;
    private final ITechnologyExternalPort technologyExternalPort;

    public BootcampUseCase(ICapabilityPersistencePort capabilityPersistencePort, IBootcampCapabilityPersistencePort bootcampCapabilityPersistencePort, ITechnologyExternalPort technologyExternalPort) {
        this.capabilityPersistencePort = capabilityPersistencePort;
        this.bootcampCapabilityPersistencePort = bootcampCapabilityPersistencePort;
        this.technologyExternalPort = technologyExternalPort;
    }

    @Override
    public Mono<Void> linkCapabilities(Long bootcampId, List<Long> capabilityIds) {
        return capabilityPersistencePort.existAllByIds(capabilityIds)
                .flatMap(result -> Boolean.FALSE.equals(result)
                        ? Mono.error(new BusinessException(TechnicalMessage.NOT_ALL_FOUND))
                        : bootcampCapabilityPersistencePort.registerBootcampCapabilities(bootcampId, capabilityIds)
                );
    }

    @Override
    public Flux<Bootcamp> getBootcampsCapabilitiesByIds(List<Long> bootcampIds) {
        return bootcampCapabilityPersistencePort.getCapabilitiesByBootcampIds(bootcampIds)
                .groupBy(BootcampCapability::getBootcampId)
                .flatMap( groupedFlux -> groupedFlux
                        .map(BootcampCapability::getCapabilityId)
                        .collectList()
                        .flatMap(capabilityIds->
                                capabilityPersistencePort.findByIds(capabilityIds)
                                        .collectList()
                                        .flatMap(capabilitiesFromDb ->
                                        technologyExternalPort.getTechnologiesByCapabilities(capabilityIds)
                                                .collectList()
                                                .map(capabilityWithTech ->
                                                    buildBootcampWithTechnologies(groupedFlux.key(), capabilitiesFromDb, capabilityWithTech)
                                        )
                                )
                        )
                );
    }

    private Bootcamp buildBootcampWithTechnologies(Long bootcampId, List<Capability> capabilitiesFromDb, List<Capability> capabilityWithTech) {
        List<Capability> capabilitiesWithTechs = capabilitiesFromDb.stream()
                .map(capability -> {
                    List<Technology> techsForCapability = capabilityWithTech.stream()
                            .filter(cap -> cap.getId().equals(capability.getId()))
                            .findAny()
                            .map(Capability::getTechnologies)
                            .orElse(Collections.emptyList());

                    return new Capability(
                            capability.getId(),
                            capability.getName(),
                            capability.getDescription(),
                            techsForCapability
                    );
                })
                .toList();

        return new Bootcamp(bootcampId, capabilitiesWithTechs);
    }
}
