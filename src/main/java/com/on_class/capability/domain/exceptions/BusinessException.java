package com.on_class.capability.domain.exceptions;

import com.on_class.capability.domain.enums.TechnicalMessage;
import lombok.Getter;

import java.util.List;

@Getter
public class BusinessException extends ProcessorException {

    private final List<String> details;

    public BusinessException(TechnicalMessage technicalMessage) {
        super(technicalMessage.getMessage(), technicalMessage);
        this.details = null;
    }

    public BusinessException(TechnicalMessage technicalMessage, List<String> details) {
        super(technicalMessage.getMessage(), technicalMessage);
        this.details = details;
    }
}
