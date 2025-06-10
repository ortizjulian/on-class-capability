package com.on_class.capability.domain.api;

import com.on_class.capability.domain.model.Bootcamp;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IBootcampServicePort {
    Mono<Void> linkCapabilities(Long bootcampId, List<Long> capabilityIds);
    Flux<Bootcamp> getBootcampsCapabilitiesByIds(List<Long> bootcampIds);
}
