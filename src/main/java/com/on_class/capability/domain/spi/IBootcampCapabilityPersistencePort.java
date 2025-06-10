package com.on_class.capability.domain.spi;

import com.on_class.capability.domain.model.BootcampCapability;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IBootcampCapabilityPersistencePort {
    Mono<Void> registerBootcampCapabilities(Long bootcampId , List<Long> capabilityIds);
    Flux<BootcampCapability> getCapabilitiesByBootcampIds(List<Long> bootcampIds);
}
