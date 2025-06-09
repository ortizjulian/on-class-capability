package com.on_class.capability.domain.usecase;

import com.on_class.capability.domain.api.IBootcampServicePort;
import com.on_class.capability.domain.enums.TechnicalMessage;
import com.on_class.capability.domain.exceptions.BusinessException;
import com.on_class.capability.domain.spi.IBootcampCapabilityPersistencePort;
import com.on_class.capability.domain.spi.ICapabilityPersistencePort;
import reactor.core.publisher.Mono;

import java.util.List;

public class BootcampUseCase implements IBootcampServicePort {

    private final ICapabilityPersistencePort capabilityPersistencePort;
    private final IBootcampCapabilityPersistencePort bootcampCapabilityPersistencePort;

    public BootcampUseCase(ICapabilityPersistencePort capabilityPersistencePort, IBootcampCapabilityPersistencePort bootcampCapabilityPersistencePort) {
        this.capabilityPersistencePort = capabilityPersistencePort;
        this.bootcampCapabilityPersistencePort = bootcampCapabilityPersistencePort;
    }

    @Override
    public Mono<Void> linkCapabilities(Long bootcampId, List<Long> capabilityIds) {
        return capabilityPersistencePort.existAllByIds(capabilityIds)
                .flatMap(result -> Boolean.FALSE.equals(result)
                        ? Mono.error(new BusinessException(TechnicalMessage.NOT_ALL_FOUND))
                        : bootcampCapabilityPersistencePort.registerBootcampCapabilities(bootcampId, capabilityIds)
                );
    }
}
