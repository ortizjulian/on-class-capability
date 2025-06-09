package com.on_class.capability.infrastructure.adapters.webclient.mapper;

import com.on_class.capability.domain.model.Capability;
import com.on_class.capability.infrastructure.adapters.webclient.dto.CapabilityResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ICapabilityResponseMapper {
    Capability toCapability(CapabilityResponseDto capabilityResponseDto);
}
