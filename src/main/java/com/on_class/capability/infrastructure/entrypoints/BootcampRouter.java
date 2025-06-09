package com.on_class.capability.infrastructure.entrypoints;

import com.on_class.capability.infrastructure.entrypoints.documentation.BootcampApiInfo;
import com.on_class.capability.infrastructure.entrypoints.handler.BootcampHandler;
import com.on_class.capability.infrastructure.utils.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class BootcampRouter {
    @Bean
    @BootcampApiInfo
    public RouterFunction<ServerResponse> bootcampRoutes(BootcampHandler bootcampHandler) {
        return route(POST(Constants.ROUTE_BOOTCAMP_LINK_CAPABILITIES),
                     bootcampHandler::assignCapabilitiesToBootcamp);
    }
}