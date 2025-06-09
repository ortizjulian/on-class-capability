package com.on_class.capability.infrastructure.adapters.webclient.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetCapabilitiesTechnologiesRequestDto {
    private List<Long> capabilityIds;
}
