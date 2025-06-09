package com.on_class.capability.infrastructure.adapters.webclient.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostCapabilityTechnologiesRequestDto {
    private List<Long> technologyIds;
}
