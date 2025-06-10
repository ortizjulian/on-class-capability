package com.on_class.capability.infrastructure.adapters.persistence.repository;

import com.on_class.capability.infrastructure.adapters.persistence.entity.CapabilityEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public interface ICapabilityRepository extends ReactiveCrudRepository<CapabilityEntity, Long>{
    Mono<CapabilityEntity> findByName(String name);
    Mono<Long> countByIdIn(List<Long> ids);
    Flux<CapabilityEntity> findAllBy(Pageable pageable);
    Flux<CapabilityEntity> findByIdIn(List<Long> ids);
}