package com.on_class.capability.infrastructure.adapters.persistence;

import com.on_class.capability.domain.model.Capability;
import com.on_class.capability.domain.spi.ICapabilityPersistencePort;
import com.on_class.capability.infrastructure.adapters.persistence.mapper.ICapabilityEntityMapper;
import com.on_class.capability.infrastructure.adapters.persistence.repository.ICapabilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CapabilityAdapter implements ICapabilityPersistencePort {

    private final ICapabilityRepository capabilityRepository;
    private final ICapabilityEntityMapper capabilityEntityMapper;

    @Override
    public Mono<Capability> createCapability(Capability capability) {
        return capabilityRepository.save(capabilityEntityMapper.toCapabilityEntity(capability)).
                map(capabilityEntityMapper::toCapability);
    }

    @Override
    public Mono<Void> deleteCapabilityById(Long id){
        return capabilityRepository.findById(id)
                .flatMap(capabilityRepository::delete);
    }

    @Override
    public Mono<Capability> findByName(String name) {
        return capabilityRepository.findByName(name)
                .map(capabilityEntityMapper::toCapability);
    }
}
