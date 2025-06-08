package com.on_class.capability.infrastructure.entrypoints.mapper;

import com.on_class.capability.domain.model.Capability;
import com.on_class.capability.domain.model.Technology;
import com.on_class.capability.infrastructure.entrypoints.dto.CapabilityRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ICapabilityMapper {
    default Capability toCapability(CapabilityRequestDto capabilityRequestDto) {
        List<Technology> technologies =  capabilityRequestDto.getTechnologyIds().stream().map(Technology::new).toList();

        Capability capability = new Capability();
        capability.setName(capabilityRequestDto.getName());
        capability.setDescription(capabilityRequestDto.getDescription());
        capability.setTechnologies(technologies);

        return capability;
    }

    CapabilityRequestDto toCapabilityRequestDto(Capability capability);
}
