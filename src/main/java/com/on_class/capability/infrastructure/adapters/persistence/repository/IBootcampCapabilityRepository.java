package com.on_class.capability.infrastructure.adapters.persistence.repository;

import com.on_class.capability.infrastructure.adapters.persistence.entity.BootcampCapabilityEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface IBootcampCapabilityRepository extends ReactiveCrudRepository<BootcampCapabilityEntity, Long> {
}
