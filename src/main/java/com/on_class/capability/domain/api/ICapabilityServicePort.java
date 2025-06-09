package com.on_class.capability.domain.api;

import com.on_class.capability.domain.model.Capability;
import com.on_class.capability.domain.model.PaginationAndFilter;
import com.on_class.capability.domain.model.PaginationResponse;
import reactor.core.publisher.Mono;

public interface ICapabilityServicePort {
    Mono<Void> createCapability(Capability capability);
    Mono<PaginationResponse<Capability>> getCapabilities(PaginationAndFilter paginationAndFilter);
}
