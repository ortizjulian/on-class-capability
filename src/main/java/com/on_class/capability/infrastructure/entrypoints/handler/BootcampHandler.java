package com.on_class.capability.infrastructure.entrypoints.handler;

import com.on_class.capability.domain.api.IBootcampServicePort;
import com.on_class.capability.domain.enums.TechnicalMessage;
import com.on_class.capability.domain.exceptions.BusinessException;
import com.on_class.capability.infrastructure.entrypoints.dto.BootcampCapabilitiesRequestDto;
import com.on_class.capability.infrastructure.entrypoints.dto.BootcampListRequestDto;
import com.on_class.capability.infrastructure.entrypoints.mapper.IBootcampResponseMapper;
import com.on_class.capability.infrastructure.entrypoints.util.ErrorResponseBuilder;
import com.on_class.capability.infrastructure.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.on_class.capability.infrastructure.utils.Constants.CAPABILITY_ERROR;

@Component
@RequiredArgsConstructor
@Slf4j
public class BootcampHandler {

    private final IBootcampServicePort bootcampServicePort;
    private final ErrorResponseBuilder responseBuilder;
    private final IBootcampResponseMapper bootcampResponseMapper;

    public Mono<ServerResponse> assignCapabilitiesToBootcamp(ServerRequest request) {
        String id = request.pathVariable(Constants.BOOTCAMP_ID_PATH_VARIABLE);
        Long bootcampId = Long.parseLong(id);

        return request.bodyToMono(BootcampCapabilitiesRequestDto.class)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.EMPTY_BODY)))
                .flatMap(requestDto -> bootcampServicePort.linkCapabilities(bootcampId,requestDto.getCapabilityIds()))
                .then(ServerResponse.status(HttpStatus.CREATED).build())
                .doOnError(ex -> log.error(CAPABILITY_ERROR, ex))
                .onErrorResume(BusinessException.class , ex ->  responseBuilder.buildErrorResponse(
                        ex.getTechnicalMessage(),
                        ex.getDetails()
                ))
                .onErrorResume(ex ->  responseBuilder.buildErrorResponse(
                        TechnicalMessage.INTERNAL_ERROR
                ));
    }

    public Mono<ServerResponse> getCapabilitiesByIds(ServerRequest request) {
        return request.bodyToMono(BootcampListRequestDto.class)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.EMPTY_BODY)))
                .flatMapMany(requestDto -> bootcampServicePort.getBootcampsCapabilitiesByIds(requestDto.getBootcampIds()))
                .map(bootcampResponseMapper::toBootcampRequestDto)
                .collectList()
                .flatMap(bootcamps -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(bootcamps))
                .doOnError(ex -> log.error(CAPABILITY_ERROR, ex))
                .onErrorResume(BusinessException.class , ex ->  responseBuilder.buildErrorResponse(
                        ex.getTechnicalMessage(),
                        ex.getDetails()
                ))
                .onErrorResume(ex ->  responseBuilder.buildErrorResponse(
                        TechnicalMessage.INTERNAL_ERROR
                ));
    }
}
