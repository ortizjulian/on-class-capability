package com.on_class.capability.infrastructure.entrypoints.dto;

import lombok.Data;

import java.util.List;

@Data
public class CapabilityResponseDto {
    private String name;
    private Long id;
    private List<TechnologyResponseDto> technologies;
}
