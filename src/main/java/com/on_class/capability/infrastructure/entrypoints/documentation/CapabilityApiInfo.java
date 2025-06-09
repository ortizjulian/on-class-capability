package com.on_class.capability.infrastructure.entrypoints.documentation;

import com.on_class.capability.infrastructure.entrypoints.dto.CapabilityRequestDto;
import com.on_class.capability.infrastructure.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@RouterOperation(
    method = RequestMethod.POST,
    path = Constants.CAPABILITY_ROUTE,
    operation = @Operation(
        summary = "Create a new capability",
        description = "Registers a capability with its relevant data.",
        operationId = "createCapability",
        tags = {"Capabilities"},
        requestBody = @RequestBody(
            description = "CapabilityRequestDto object",
            required = true,
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = CapabilityRequestDto.class)
            )
        ),
        responses = {
                @ApiResponse(
                        responseCode = "201",
                        description = "Capability created successfully"
                ),
                @ApiResponse(
                        responseCode = "400",
                        description = "Invalid request data"
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "Not all technologies found, please verify data "

                ),
                @ApiResponse(
                        responseCode = "500",
                        description = "Unexpected server error"
                )
        }
    )
)
public @interface CapabilityApiInfo {}
