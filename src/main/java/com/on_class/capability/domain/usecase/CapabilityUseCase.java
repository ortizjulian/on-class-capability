package com.on_class.capability.domain.usecase;

import com.on_class.capability.domain.api.ICapabilityServicePort;
import com.on_class.capability.domain.constants.DomainConstants;
import com.on_class.capability.domain.enums.TechnicalMessage;
import com.on_class.capability.domain.exceptions.BusinessException;
import com.on_class.capability.domain.model.*;
import com.on_class.capability.domain.spi.ICapabilityPersistencePort;
import com.on_class.capability.domain.spi.ITechnologyExternalPort;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @Override
    public Mono<PaginationResponse<Capability>> getPaginatedCapabilities(PaginationAndFilter paginationAndFilter) {
        return validatePaginationAndFilter(paginationAndFilter)
                .flatMap(validated -> capabilityPersistencePort.getCapabilities(paginationAndFilter))
                .flatMap(this::enrichCapabilitiesWithTechnologies);
    }

    private Mono<PaginationResponse<Capability>> enrichCapabilitiesWithTechnologies(PaginationResponse<Capability> paginatedCapabilities) {
        List<Long> capabilityIds = paginatedCapabilities.getElements()
                .stream()
                .map(Capability::getId)
                .toList();

        if (capabilityIds.isEmpty()) {
            return Mono.just(paginatedCapabilities);
        }

        return technologyExternalPort.getTechnologiesByCapabilities(capabilityIds)
                .collectList()
                .map(techCapabilities -> buildNewPaginationResponse(paginatedCapabilities, techCapabilities));
    }

    private PaginationResponse<Capability> buildNewPaginationResponse(PaginationResponse<Capability> paginatedCapabilities,
                                                                      List<Capability> techCapabilities) {
        Map<Long, Capability> techCapabilityMap = techCapabilities.stream()
                .collect(Collectors.toMap(Capability::getId, Function.identity()));

        List<Capability> newCapabilities = paginatedCapabilities.getElements().stream()
                .map(cap -> {
                    List<Technology> technologies = Optional.ofNullable(techCapabilityMap.get(cap.getId()))
                            .map(Capability::getTechnologies)
                            .orElse(Collections.emptyList());

                    return new Capability(
                            cap.getId(),
                            cap.getName(),
                            cap.getDescription(),
                            technologies
                    );
                })
                .toList();

        return new PaginationResponse<>(
                paginatedCapabilities.getTotalPages(),
                paginatedCapabilities.getCurrentPage(),
                paginatedCapabilities.getTotalElements(),
                newCapabilities
        );
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

    private Mono<PaginationAndFilter> validatePaginationAndFilter(PaginationAndFilter paginationAndFilter) {

        List<String> errors = new ArrayList<>();

        if (paginationAndFilter.getSize() <= DomainConstants.PAGINATION_MIN_SIZE) {
            errors.add(DomainConstants.EXCEPTION_PAGINATION_SIZE);
        }

        if (paginationAndFilter.getPage() < DomainConstants.PAGINATION_MIN_PAGE) {
            errors.add(DomainConstants.EXCEPTION_PAGINATION_PAGE);
        }

        Set<String> validSortDirections = Set.of(DomainConstants.SORT_BY_ASC, DomainConstants.SORT_BY_DESC);

        if (!validSortDirections.contains(paginationAndFilter.getSortDirection().toUpperCase())) {
            errors.add(DomainConstants.EXCEPTION_PAGINATION_SORT);
        }

        Set<String> validSortFields = Set.of(DomainConstants.CAPABILITY_SORT_BY_NAME, DomainConstants.CAPABILITY_SORT_BY_TECHNOLOGY_QUANTITY);

        if (!validSortFields.contains(paginationAndFilter.getSortField().toLowerCase())) {
            errors.add(DomainConstants.EXCEPTION_PAGINATION_SORT_FIELD);
        }

        if (!errors.isEmpty()) {
            return Mono.error(new BusinessException(TechnicalMessage.ADAPTER_RESPONSE_PAGINATION_BAD_REQUEST,errors));
        }

        return Mono.just(paginationAndFilter);
    }
}
