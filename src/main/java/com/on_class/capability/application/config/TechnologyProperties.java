package com.on_class.capability.application.config;

import com.on_class.capability.infrastructure.utils.Constants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = Constants.PROPERTIES_PREFIX_TECHNOLOGY)
@Getter
@Setter
public class TechnologyProperties {

    private String baseUrl;

}