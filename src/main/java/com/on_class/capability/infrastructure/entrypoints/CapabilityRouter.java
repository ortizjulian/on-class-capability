package com.on_class.capability.infrastructure.entrypoints;

import com.on_class.capability.infrastructure.entrypoints.documentation.CapabilityApiInfo;
import com.on_class.capability.infrastructure.entrypoints.handler.CapabilityHandler;
import com.on_class.capability.infrastructure.utils.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;

@Configuration
public class CapabilityRouter {
    @Bean
    @CapabilityApiInfo
    public RouterFunction<ServerResponse> routerFunction(CapabilityHandler capabilityHandler) {
        return nest(path(Constants.CAPABILITY_ROUTE),
                route(POST(Constants.ROUTE_EMPTY), capabilityHandler::createCapability)
                        .andRoute(GET(Constants.ROUTE_EMPTY),capabilityHandler::getPaginatedCapabilities));
    }
}
