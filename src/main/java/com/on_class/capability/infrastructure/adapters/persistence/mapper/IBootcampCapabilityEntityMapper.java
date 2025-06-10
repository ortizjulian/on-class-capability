package com.on_class.capability.infrastructure.adapters.persistence.mapper;

import com.on_class.capability.domain.model.BootcampCapability;
import com.on_class.capability.infrastructure.adapters.persistence.entity.BootcampCapabilityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IBootcampCapabilityEntityMapper {

    BootcampCapability toBootcampCapability(BootcampCapabilityEntity bootcampCapabilityEntity);
}
