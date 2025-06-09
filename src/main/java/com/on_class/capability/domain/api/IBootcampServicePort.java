package com.on_class.capability.domain.api;

import reactor.core.publisher.Mono;

import java.util.List;

public interface IBootcampServicePort {
    Mono<Void> linkCapabilities(Long bootcampId, List<Long> capabilityIds);
}
