package com.on_class.capability.infrastructure.adapters.webclient.mapper;

import com.on_class.capability.domain.model.Technology;
import com.on_class.capability.infrastructure.adapters.webclient.dto.CapabilityTechnologiesRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ICapabilityTechnologiesMapper {

    default CapabilityTechnologiesRequestDto toCapabilityTechnologiesRequestDto(List<Technology> technologies) {
        return CapabilityTechnologiesRequestDto
                .builder()
                .technologyIds(technologies.stream().map(Technology::getId).toList())
                .build();
    }
}
