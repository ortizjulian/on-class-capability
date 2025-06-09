package com.on_class.capability.domain.spi;

import reactor.core.publisher.Mono;

import java.util.List;

public interface IBootcampCapabilityPersistencePort {
    Mono<Void> registerBootcampCapabilities(Long bootcampId , List<Long> capabilityIds);
}
