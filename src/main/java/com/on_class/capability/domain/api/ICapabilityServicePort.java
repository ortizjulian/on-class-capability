package com.on_class.capability.domain.api;

import com.on_class.capability.domain.model.Capability;
import reactor.core.publisher.Mono;

public interface ICapabilityServicePort {
    Mono<Void> createCapability(Capability capability);
}
