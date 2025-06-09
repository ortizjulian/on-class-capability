package com.on_class.capability.domain.spi;

import com.on_class.capability.domain.model.Capability;
import com.on_class.capability.domain.model.PaginationAndFilter;
import com.on_class.capability.domain.model.PaginationResponse;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ICapabilityPersistencePort {
    Mono<Capability> createCapability(Capability capability);
    Mono<Void> deleteCapabilityById(Long id);
    Mono<Capability> findByName(String name);
    Mono<PaginationResponse<Capability>> getCapabilities(PaginationAndFilter paginationAndFilter);
    Mono<Boolean> existAllByIds(List<Long> ids);
}
