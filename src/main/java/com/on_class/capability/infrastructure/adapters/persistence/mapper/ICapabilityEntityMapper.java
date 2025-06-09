package com.on_class.capability.infrastructure.adapters.persistence.mapper;

import com.on_class.capability.domain.model.Capability;
import com.on_class.capability.infrastructure.adapters.persistence.entity.CapabilityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ICapabilityEntityMapper {
    CapabilityEntity toCapabilityEntity(Capability capability);
    Capability toCapability(CapabilityEntity capabilityEntity);
}
