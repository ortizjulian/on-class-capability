package com.on_class.capability.infrastructure.entrypoints.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;

@Builder
public record ErrorDto(String code, String message, @JsonInclude(JsonInclude.Include.NON_NULL) List<String> details) {
}