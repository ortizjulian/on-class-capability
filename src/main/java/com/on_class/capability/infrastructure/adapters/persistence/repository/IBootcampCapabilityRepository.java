package com.on_class.capability.infrastructure.adapters.persistence.repository;

import com.on_class.capability.infrastructure.adapters.persistence.entity.BootcampCapabilityEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.List;

public interface IBootcampCapabilityRepository extends ReactiveCrudRepository<BootcampCapabilityEntity, Long> {
    Flux<BootcampCapabilityEntity> findByBootcampIdIn(List<Long> bootcampIds);
}
