package com.on_class.capability.infrastructure.entrypoints.dto;

import lombok.Data;

import java.util.List;

@Data
public class BootcampResponseDto {
    private Long id;
    private List<CapabilityResponseDto> capabilities;
}
