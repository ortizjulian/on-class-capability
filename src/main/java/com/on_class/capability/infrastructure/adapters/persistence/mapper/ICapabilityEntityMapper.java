package com.on_class.capability.infrastructure.adapters.persistence.mapper;

import com.on_class.capability.domain.model.Capability;
import com.on_class.capability.infrastructure.adapters.persistence.entity.CapabilityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ICapabilityEntityMapper {
    default CapabilityEntity toCapabilityEntity(Capability capability) {
        return CapabilityEntity.builder()
                .name(capability.getName())
                .description(capability.getDescription())
                .technologyQuantity(capability.getTechnologies().size())
                .build();

    }
    Capability toCapability(CapabilityEntity capabilityEntity);

    List<Capability> toCapabilities(List<CapabilityEntity> capabilityEntityList);
}
