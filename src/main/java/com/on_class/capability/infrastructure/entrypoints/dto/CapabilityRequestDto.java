package com.on_class.capability.infrastructure.entrypoints.dto;

import com.on_class.capability.infrastructure.utils.Constants;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CapabilityRequestDto {

    @NotBlank(message = Constants.EXCEPTION_CAPABILITY_NAME_NULL)
    private String name;

    @NotBlank(message = Constants.EXCEPTION_CAPABILITY_DESCRIPTION_NULL)
    private String description;

    private List<Long> technologyIds;
}
