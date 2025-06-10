package com.on_class.capability.infrastructure.entrypoints.documentation;

import com.on_class.capability.infrastructure.entrypoints.dto.BootcampCapabilitiesRequestDto;
import com.on_class.capability.infrastructure.entrypoints.dto.BootcampListRequestDto;
import com.on_class.capability.infrastructure.entrypoints.dto.BootcampResponseDto;
import com.on_class.capability.infrastructure.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
                path = Constants.BOOTCAMP_ROUTE + Constants.ROUTE_BOOTCAMP_LINK_CAPABILITIES,
                operation = @Operation(
                        summary = "Link capabilities to a bootcamp",
                        description = "Associates a list of capabilities IDs with a given bootcamp.",
                        operationId = "linkCapacities",
                        tags = {"BootCamp"},
                        parameters = {
                                @Parameter(
                                        name = Constants.BOOTCAMP_ID_PATH_VARIABLE,
                                        description = "The ID of the bootcamp to link capabilities to",
                                        required = true,
                                        in = ParameterIn.PATH
                                )
                        },
                        requestBody = @RequestBody(
                                description = "A list of capabilities IDs to be linked to the bootcamp",
                                required = true,
                                content = @Content(
                                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        schema = @Schema(
                                                implementation = BootcampCapabilitiesRequestDto.class,
                                                requiredProperties = {"capabilityIds"}
                                        )
                                )
                        ),
                        responses = {
                                @ApiResponse(
                                        responseCode = "201",
                                        description = "Capabilities linked successfully"
                                ),
                                @ApiResponse(
                                        responseCode = "400",
                                        description = "Invalid request data"
                                ),
                                @ApiResponse(
                                        responseCode = "404",
                                        description = "One or more Capability IDs not found"
                                ),
                                @ApiResponse(
                                        responseCode = "500",
                                        description = "Unexpected server error"
                                )
                        }
                )
        ),
        @RouterOperation(
                method = RequestMethod.POST,
                path =  Constants.BOOTCAMP_ROUTE + Constants.ROUTE_BY_IDS,
                operation = @Operation(
                        summary = "Get bootcamps linked capabilities",
                        description = "Retrieves a list of capabilities associated with a given bootcamps.",
                        operationId = "getBootcampLinkedCapabilities",
                        tags = {"BootCamp"},
                        requestBody = @RequestBody(
                                description = "A list of bootcamps IDs",
                                required = true,
                                content = @Content(
                                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        schema = @Schema(
                                                implementation = BootcampListRequestDto.class,
                                                requiredProperties = {"bootcampIds"}
                                        )
                                )
                        ),
                        responses = {
                                @ApiResponse(
                                        responseCode = "200",
                                        description = "List of linked capabilities to each bootcamp retrieved successfully",
                                        content = @Content(
                                                mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                array = @ArraySchema(schema = @Schema(implementation = BootcampResponseDto.class))
                                        )
                                ),
                                @ApiResponse(
                                        responseCode = "404",
                                        description = "Invalid request (e.g., empty body)"
                                ),
                                @ApiResponse(
                                        responseCode = "500",
                                        description = "Unexpected server error"
                                )
                        }
                )

        )
})
public @interface BootcampApiInfo {}
