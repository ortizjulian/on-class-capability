package com.on_class.capability.infrastructure.adapters.persistence;

import com.on_class.capability.domain.model.BootcampCapability;
import com.on_class.capability.domain.spi.IBootcampCapabilityPersistencePort;
import com.on_class.capability.infrastructure.adapters.persistence.entity.BootcampCapabilityEntity;
import com.on_class.capability.infrastructure.adapters.persistence.mapper.IBootcampCapabilityEntityMapper;
import com.on_class.capability.infrastructure.adapters.persistence.repository.IBootcampCapabilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BootcampCapabilityAdapter implements IBootcampCapabilityPersistencePort {

    private final IBootcampCapabilityRepository bootcampCapabilityRepository;
    private final IBootcampCapabilityEntityMapper bootcampCapabilityEntityMapper;
    @Override
    public Mono<Void> registerBootcampCapabilities(Long bootcampId, List<Long> capabilityIds) {
        return Flux.fromIterable(capabilityIds)
                .map(capabilityId -> BootcampCapabilityEntity.builder()
                        .bootcampId(bootcampId)
                        .capabilityId(capabilityId)
                        .build()
                ).collectList()
                .flatMapMany(bootcampCapabilityRepository::saveAll)
                .then();
    }

    @Override
    public Flux<BootcampCapability> getCapabilitiesByBootcampIds(List<Long> bootcampIds) {
        return bootcampCapabilityRepository.findByBootcampIdIn(bootcampIds)
                .map(bootcampCapabilityEntityMapper::toBootcampCapability);
    }
}
