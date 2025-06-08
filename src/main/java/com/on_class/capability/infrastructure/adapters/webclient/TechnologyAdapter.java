package com.on_class.capability.infrastructure.adapters.webclient;

import com.on_class.capability.domain.enums.TechnicalMessage;
import com.on_class.capability.domain.exceptions.BusinessException;
import com.on_class.capability.domain.exceptions.TechnicalException;
import com.on_class.capability.domain.model.Technology;
import com.on_class.capability.domain.spi.ITechnologyExternalPort;
import com.on_class.capability.infrastructure.adapters.webclient.dto.ErrorResponse;
import com.on_class.capability.infrastructure.adapters.webclient.mapper.ICapabilityTechnologiesMapper;
import com.on_class.capability.infrastructure.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TechnologyAdapter implements ITechnologyExternalPort {

    private final WebClient webClient;
    private final ICapabilityTechnologiesMapper capabilityTechnologiesMapper;

    @Override
    public Mono<Void> linkTechnologiesToCapability(Long idCapability, List<Technology> technologies) {
        return webClient.post()
                .uri(Constants.TECHNOLOGY_ROUTE + idCapability)
                .bodyValue(capabilityTechnologiesMapper.toCapabilityTechnologiesRequestDto(technologies))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> buildErrorResponse(response, TechnicalMessage.ADAPTER_RESPONSE_NOT_FOUND))
                .onStatus(HttpStatusCode::is5xxServerError, response -> buildErrorResponse(response, TechnicalMessage.INTERNAL_ERROR_IN_ADAPTERS))
                .toBodilessEntity()
                .then();
    }

    private Mono<Throwable> buildErrorResponse(ClientResponse response, TechnicalMessage technicalMessage) {
        return response.bodyToMono(ErrorResponse.class)
                .defaultIfEmpty(ErrorResponse.builder().message(Constants.NO_ADDITIONAL_DETAILS).build())
                .flatMap(errorBody -> Mono.error(
                            response.statusCode().is5xxServerError() ?
                                    new TechnicalException(technicalMessage):
                                    new BusinessException(technicalMessage,List.of(errorBody.getMessage())))
                );
    }
}
