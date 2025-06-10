package com.on_class.capability.domain.spi;

import com.on_class.capability.domain.model.Capability;
import com.on_class.capability.domain.model.Technology;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ITechnologyExternalPort {
    Mono<Void> linkTechnologiesToCapability(Long idCapability, List<Technology> technologies);
    Flux<Capability> getTechnologiesByCapabilities(List<Long> capabilityIds);
}
