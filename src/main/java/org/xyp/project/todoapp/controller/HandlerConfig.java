package org.xyp.project.todoapp.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

@Slf4j
@Configuration
public class HandlerConfig {

    @Bean
    public RouterFunction<ServerResponse> getRouterFunction() {
        log.info("route 1 ......");
        return RouterFunctions.route()
          .GET("/api/v1/fun/hello", req -> ServerResponse.ok().body("this is from route"))

          .build();
    }
    @Bean
    public RouterFunction<ServerResponse> getRouterFunction2() {
        log.info("route 2 ......");
        return RouterFunctions.route()
          .GET("/api/v1/fun/hello2", req -> ServerResponse.ok().body("this is from route 2"))

          .build();
    }
}
