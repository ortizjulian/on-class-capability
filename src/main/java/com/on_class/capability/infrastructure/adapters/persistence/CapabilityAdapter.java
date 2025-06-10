package com.on_class.capability.infrastructure.adapters.persistence;

import com.on_class.capability.domain.model.Capability;
import com.on_class.capability.domain.model.PaginationAndFilter;
import com.on_class.capability.domain.model.PaginationResponse;
import com.on_class.capability.domain.spi.ICapabilityPersistencePort;
import com.on_class.capability.infrastructure.adapters.persistence.mapper.ICapabilityEntityMapper;
import com.on_class.capability.infrastructure.adapters.persistence.repository.ICapabilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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

    @Override
    public Mono<PaginationResponse<Capability>> getCapabilities(PaginationAndFilter paginationAndFilter) {
        Pageable pageable = PageRequest.of(
                paginationAndFilter.getPage(),
                paginationAndFilter.getSize(),
                Sort.by(Sort.Direction.fromString(paginationAndFilter.getSortDirection()),
                        paginationAndFilter.getSortField())
        );

        return capabilityRepository.findAllBy(pageable)
                .collectList()
                .zipWith(capabilityRepository.count())
                .map(zip -> {
                    List<Capability> capabilities = capabilityEntityMapper.toCapabilities(zip.getT1());
                    long totalElements = zip.getT2();
                    int size = pageable.getPageSize();
                    int currentPage = pageable.getPageNumber();
                    int totalPages = (int) ((totalElements + size - 1) / size);

                    return new PaginationResponse<>(totalPages,currentPage,totalElements,capabilities);
                });

    }

    @Override
    public Mono<Boolean> existAllByIds(List<Long> ids) {
        return capabilityRepository.countByIdIn(ids)
                .map(count -> count == ids.size());
    }

    @Override
    public Flux<Capability> findByIds(List<Long> capabilityIds) {
        return capabilityRepository.findByIdIn(capabilityIds)
                .map(capabilityEntityMapper::toCapability);
    }
}
