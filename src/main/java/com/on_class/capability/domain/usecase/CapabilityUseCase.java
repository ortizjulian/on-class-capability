package com.on_class.capability.domain.usecase;

import com.on_class.capability.domain.api.ICapabilityServicePort;
import com.on_class.capability.domain.constants.DomainConstants;
import com.on_class.capability.domain.enums.TechnicalMessage;
import com.on_class.capability.domain.exceptions.BusinessException;
import com.on_class.capability.domain.model.Capability;
import com.on_class.capability.domain.model.Technology;
import com.on_class.capability.domain.spi.ICapabilityPersistencePort;
import com.on_class.capability.domain.spi.ITechnologyExternalPort;
import reactor.core.publisher.Mono;

import java.util.List;

public class CapabilityUseCase implements ICapabilityServicePort {

    private final ICapabilityPersistencePort capabilityPersistencePort;
    private final ITechnologyExternalPort technologyExternalPort;

    public CapabilityUseCase(ICapabilityPersistencePort capabilityPersistencePort, ITechnologyExternalPort technologyExternalPort) {
        this.capabilityPersistencePort = capabilityPersistencePort;
        this.technologyExternalPort = technologyExternalPort;
    }

    @Override
    public Mono<Void> createCapability(Capability capability) {
        return validateTechnologies(capability)
                .flatMap(validatedCapability -> capabilityPersistencePort.findByName(validatedCapability.getName()))
                .flatMap(existing -> Mono.error(new BusinessException(TechnicalMessage.ALREADY_EXISTS)))
                .switchIfEmpty(
                        Mono.defer(() ->
                                capabilityPersistencePort.createCapability(capability)
                                        .flatMap(createdCapability ->
                                                technologyExternalPort.linkTechnologiesToCapability(
                                                                createdCapability.getId(), capability.getTechnologies()
                                                        )
                                                        .onErrorResume(ex -> rollbackCapabilityCreation(createdCapability.getId(), ex))
                                        )
                        )
                )
                .then();
    }

    private Mono<Void> rollbackCapabilityCreation(Long id, Throwable ex) {
        return capabilityPersistencePort.deleteCapabilityById(id)
                .then(Mono.error(ex));
    }

    private Mono<Capability> validateTechnologies(Capability capability) {

        List<Technology> technologies = capability.getTechnologies();
        if (technologies.size() < DomainConstants.MIN_TECHNOLOGIES || technologies.size() > DomainConstants.MAX_TECHNOLOGIES) {
            return Mono.error(new BusinessException(TechnicalMessage.INVALID_REQUEST,List.of(DomainConstants.EXCEPTION_TECHNOLOGY_INVALID_QUANTITY)));
        }

        long uniqueIdsCount = technologies.stream()
                .map(Technology::getId)
                .distinct()
                .count();

        if (uniqueIdsCount != technologies.size()) {
            return Mono.error(new BusinessException(TechnicalMessage.INVALID_REQUEST,List.of(DomainConstants.EXCEPTION_TECHNOLOGY_DUPLICATED_ID)));
        }
        return Mono.just(capability);
    }
}
