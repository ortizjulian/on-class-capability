package com.on_class.capability.application.config;

import com.on_class.capability.domain.api.IBootcampServicePort;
import com.on_class.capability.domain.api.ICapabilityServicePort;
import com.on_class.capability.domain.spi.IBootcampCapabilityPersistencePort;
import com.on_class.capability.domain.spi.ICapabilityPersistencePort;
import com.on_class.capability.domain.spi.ITechnologyExternalPort;
import com.on_class.capability.domain.usecase.BootcampUseCase;
import com.on_class.capability.domain.usecase.CapabilityUseCase;
import com.on_class.capability.infrastructure.adapters.webclient.TechnologyAdapter;
import com.on_class.capability.infrastructure.adapters.webclient.mapper.ICapabilityResponseMapper;
import com.on_class.capability.infrastructure.adapters.webclient.mapper.ICapabilityTechnologiesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final ICapabilityPersistencePort capabilityPersistencePort;
    private final IBootcampCapabilityPersistencePort bootcampCapabilityPersistencePort;
    private final ICapabilityTechnologiesMapper capabilityTechnologiesMapper;
    private final ICapabilityResponseMapper capabilityResponseMapper;
    private final TechnologyProperties technologyProperties;

    @Bean
    public ICapabilityServicePort capabilityServicePort(ITechnologyExternalPort technologyExternalPort) {
        return new CapabilityUseCase(capabilityPersistencePort,technologyExternalPort);
    }

    @Bean
    public ITechnologyExternalPort technologyExternalPort(WebClient webClient) {
        return new TechnologyAdapter(webClient, capabilityTechnologiesMapper,capabilityResponseMapper);
    }

    @Bean
    public IBootcampServicePort bootcampServicePort(){
        return new BootcampUseCase(capabilityPersistencePort, bootcampCapabilityPersistencePort);
    }

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.baseUrl(technologyProperties.getBaseUrl()).build();
    }
}
