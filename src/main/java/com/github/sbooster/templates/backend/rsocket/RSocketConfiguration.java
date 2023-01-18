package com.github.sbooster.templates.backend.rsocket;

import com.github.sbooster.templates.backend.rsocket.handler.ExceptionMessageHandlerAdvice;
import com.github.sbooster.templates.backend.rsocket.security.crypt.Jackson2JsonUnderByteCryptDecoder;
import com.github.sbooster.templates.backend.rsocket.security.crypt.Jackson2JsonUnderByteCryptEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.security.messaging.handler.invocation.reactive.AuthenticationPrincipalArgumentResolver;
import org.springframework.web.method.ControllerAdviceBean;
import org.springframework.web.util.pattern.PathPatternRouteMatcher;

@Configuration
public class RSocketConfiguration {
    @Bean
    public RSocketStrategies getRSocketStrategies(@Value("${sbooster.messaging.crypt}") Boolean crypt) {
        return RSocketStrategies.builder()
                .encoder(crypt ? new Jackson2JsonUnderByteCryptEncoder() : new Jackson2JsonEncoder())
                .decoder(crypt ? new Jackson2JsonUnderByteCryptDecoder() : new Jackson2JsonDecoder())
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
