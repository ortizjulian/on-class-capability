package com.on_class.capability.infrastructure.adapters.webclient.dto;

import lombok.Data;

import java.util.List;

@Data
public class CapabilityResponseDto {
    private Long id;
    private List<TechnologyResponseDto> technologies;
}
