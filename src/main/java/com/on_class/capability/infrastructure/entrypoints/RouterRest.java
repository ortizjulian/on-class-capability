package com.on_class.capability.infrastructure.entrypoints;

import com.on_class.capability.infrastructure.entrypoints.handler.CapabilityHandler;
import com.on_class.capability.infrastructure.utils.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(CapabilityHandler capabilityHandler) {
        return route(POST(Constants.CAPABILITY_ROUTE), capabilityHandler::createCapability);
    }
}
