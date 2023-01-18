package com.github.sbooster.templates.backend.rsocket.security;

import com.auth0.jwt.algorithms.Algorithm;
import com.github.sbooster.templates.backend.rsocket.security.bearer.BearerPayloadExchangeConverter;
import com.github.sbooster.templates.backend.rsocket.security.service.ReactiveCredentialsService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.rsocket.EnableRSocketSecurity;
import org.springframework.security.config.annotation.rsocket.PayloadInterceptorOrder;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.rsocket.authentication.AuthenticationPayloadInterceptor;
import org.springframework.security.rsocket.core.PayloadSocketAcceptorInterceptor;

@Configuration
@EnableRSocketSecurity
@EnableReactiveMethodSecurity
public class RSocketSecurityConfiguration {
    @Bean
    public PayloadSocketAcceptorInterceptor authorizationToken(RSocketSecurity rsocket, ReactiveCredentialsService<?> userDetailsService, Algorithm algorithm) {
        return rsocket
                .addPayloadInterceptor((exchange, chain) -> {
                    AuthenticationPayloadInterceptor result = new AuthenticationPayloadInterceptor(userDetailsService);
                    result.setAuthenticationConverter(new BearerPayloadExchangeConverter(userDetailsService, algorithm));
                    result.setOrder(PayloadInterceptorOrder.AUTHENTICATION.getOrder());
                    return result.intercept(exchange, chain);
                })
                .authorizePayload(authorize -> authorize.anyExchange().permitAll())
                .build();
    }

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults(Strings.EMPTY);
    }
}
