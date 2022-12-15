package com.github.sbooster.templates.backend.rsocket;

import com.github.sbooster.templates.backend.rsocket.crypt.Jackson2JsonUnderByteCryptDecoder;
import com.github.sbooster.templates.backend.rsocket.crypt.Jackson2JsonUnderByteCryptEncoder;
import com.github.sbooster.templates.backend.rsocket.handler.ExceptionMessageHandlerAdvice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.security.messaging.handler.invocation.reactive.AuthenticationPrincipalArgumentResolver;
import org.springframework.web.method.ControllerAdviceBean;
import org.springframework.web.util.pattern.PathPatternRouteMatcher;

@Slf4j
@Configuration
public class RSocketConfiguration {
    @Bean
    public RSocketStrategies getRSocketStrategies() {
        return RSocketStrategies.builder()
                .encoder(new Jackson2JsonUnderByteCryptEncoder())
                .decoder(new Jackson2JsonUnderByteCryptDecoder())
                .routeMatcher(new PathPatternRouteMatcher())
                .build();
    }

    @Bean
    public RSocketMessageHandler messageHandler(RSocketStrategies strategies, ApplicationContext context) {
        RSocketMessageHandler messageHandler = new RSocketMessageHandler();
        messageHandler.getArgumentResolverConfigurer().addCustomResolver(new AuthenticationPrincipalArgumentResolver());
        messageHandler.setRSocketStrategies(strategies);
        ControllerAdviceBean.findAnnotatedBeans(context)
                .forEach(bean -> messageHandler.registerMessagingAdvice(new ExceptionMessageHandlerAdvice(bean)));
        return messageHandler;
    }
}
