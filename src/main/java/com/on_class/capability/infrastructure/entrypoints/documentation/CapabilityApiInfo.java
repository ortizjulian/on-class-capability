package com.on_class.capability.infrastructure.entrypoints.documentation;

import com.on_class.capability.infrastructure.entrypoints.dto.CapabilityRequestDto;
import com.on_class.capability.infrastructure.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@RouterOperations({
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
        ),
        @RouterOperation(
                method = RequestMethod.GET,
                path = Constants.CAPABILITY_ROUTE,
                operation = @Operation(
                        summary = "Get capabilities list",
                        description = "Retrieve a paginated list of capabilities with their associated technologies",
                        operationId = "getCapabilities",
                        tags = {"Capabilities"},
                        parameters = {
                                @Parameter(name = Constants.QUERY_PARAM_PAGE, description = "Page number", in = ParameterIn.QUERY, required = true),
                                @Parameter(name = Constants.QUERY_PARAM_SIZE, description = "Number of records per page", in = ParameterIn.QUERY, required = true),
                                @Parameter(name = Constants.QUERY_PARAM_SORT_DIRECTION, description = "Sort direction (ASC/DESC)", in = ParameterIn.QUERY, required = true),
                                @Parameter(name = Constants.QUERY_PARAM_SORT_FIELD, description = "Field to sort by", in = ParameterIn.QUERY, required = true)
                        },
                        responses = {
                                @ApiResponse(responseCode = "200", description = "Capabilities retrieved successfully"),
                                @ApiResponse(responseCode = "400", description = "Invalid query parameters"),
                                @ApiResponse(responseCode = "500", description = "Unexpected server error")
                        }
                )
        )
})
public @interface CapabilityApiInfo {}
