package com.on_class.capability.infrastructure.entrypoints.mapper;

import com.on_class.capability.domain.model.Bootcamp;
import com.on_class.capability.infrastructure.entrypoints.dto.BootcampResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IBootcampResponseMapper {
    BootcampResponseDto toBootcampRequestDto(Bootcamp bootcamp);
}
