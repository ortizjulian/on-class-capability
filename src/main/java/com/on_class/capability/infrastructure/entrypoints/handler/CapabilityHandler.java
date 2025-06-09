package com.on_class.capability.infrastructure.entrypoints.handler;


import com.on_class.capability.domain.api.ICapabilityServicePort;
import com.on_class.capability.domain.enums.TechnicalMessage;
import com.on_class.capability.domain.exceptions.BusinessException;
import com.on_class.capability.domain.exceptions.TechnicalException;
import com.on_class.capability.domain.model.PaginationAndFilter;
import com.on_class.capability.infrastructure.entrypoints.dto.CapabilityRequestDto;
import com.on_class.capability.infrastructure.entrypoints.handler.validator.RequestValidator;
import com.on_class.capability.infrastructure.entrypoints.mapper.ICapabilityMapper;
import com.on_class.capability.infrastructure.entrypoints.util.ErrorResponseBuilder;
import com.on_class.capability.infrastructure.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.on_class.capability.infrastructure.utils.Constants.CAPABILITY_ERROR;

@Component
@RequiredArgsConstructor
@Slf4j
public class CapabilityHandler{

    private final RequestValidator requestValidator;
    private final ICapabilityServicePort capabilityServicePort;
    private final ICapabilityMapper capabilityMapper;
    private final ErrorResponseBuilder responseBuilder;

    public Mono<ServerResponse> createCapability(ServerRequest request) {
        return request.bodyToMono(CapabilityRequestDto.class)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.EMPTY_BODY)))
                .doOnNext(requestValidator::validate)
                .map(capabilityMapper::toCapability)
                .flatMap(capabilityServicePort::createCapability)
                .then(ServerResponse.status(HttpStatus.CREATED).build())
                .doOnError(ex -> log.error(CAPABILITY_ERROR, ex))
                .onErrorResume(BusinessException.class , ex ->  responseBuilder.buildErrorResponse(
                        ex.getTechnicalMessage(),
                        ex.getDetails()
                ))
                .onErrorResume(TechnicalException.class, ex ->  responseBuilder.buildErrorResponse(
                        ex.getTechnicalMessage()
                ))
                .onErrorResume(ex ->  responseBuilder.buildErrorResponse(
                        TechnicalMessage.INTERNAL_ERROR
                ));
    }

    public Mono<ServerResponse> getCapabilities(ServerRequest request) {
        PaginationAndFilter paginationAndFilter = buildPaginationAndFilter(request);

        return capabilityServicePort.getCapabilities(paginationAndFilter)
                .flatMap(capabilities -> ServerResponse.ok().bodyValue(capabilities))
                .doOnError(ex -> log.error(CAPABILITY_ERROR, ex))
                .onErrorResume(BusinessException.class , ex ->  responseBuilder.buildErrorResponse(
                        ex.getTechnicalMessage(),
                        ex.getDetails()
                ))
                .onErrorResume(TechnicalException.class, ex ->  responseBuilder.buildErrorResponse(
                        ex.getTechnicalMessage()
                ))
                .onErrorResume(ex ->  responseBuilder.buildErrorResponse(
                        TechnicalMessage.INTERNAL_ERROR
                ));
    }

    private PaginationAndFilter buildPaginationAndFilter(ServerRequest request) {
        int page = request.queryParam(Constants.QUERY_PARAM_PAGE)
                .map(Integer::parseInt)
                .orElse(Constants.DEFAULT_PAGE);

        int size = request.queryParam(Constants.QUERY_PARAM_SIZE)
                .map(Integer::parseInt)
                .orElse(Constants.DEFAULT_SIZE);

        String sortDirection = request.queryParam(Constants.QUERY_PARAM_SORT_DIRECTION)
                .orElse(Constants.DEFAULT_SORT_DIRECTION);

        String sortField = request.queryParam(Constants.QUERY_PARAM_SORT_FIELD)
                .orElse(Constants.DEFAULT_SORT_FIELD);

        return new PaginationAndFilter(page, size, sortDirection, sortField);
    }

}
