package com.github.sbooster.templates.backend.rsocket.security.bearer;

import com.github.sbooster.templates.backend.rsocket.security.service.ReactiveCredentialsService;
import com.github.sbooster.templates.backend.util.JwtUtils;
import io.netty.buffer.ByteBuf;
import io.rsocket.metadata.AuthMetadataCodec;
import io.rsocket.metadata.CompositeMetadata;
import io.rsocket.metadata.WellKnownAuthType;
import io.rsocket.metadata.WellKnownMimeType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.rsocket.api.PayloadExchange;
import org.springframework.security.rsocket.authentication.PayloadExchangeAuthenticationConverter;
import reactor.core.publisher.Mono;

public record BearerPayloadExchangeConverter(
        ReactiveCredentialsService<?> reactiveCredentialsService) implements PayloadExchangeAuthenticationConverter {
    private static final String AUTHENTICATION_MIME_TYPE_VALUE = WellKnownMimeType.MESSAGE_RSOCKET_AUTHENTICATION.getString();

    @Override
    public Mono<Authentication> convert(PayloadExchange exchange) {
        ByteBuf metadata = exchange.getPayload().metadata();
        CompositeMetadata compositeMetadata = new CompositeMetadata(metadata, false);
        for (CompositeMetadata.Entry entry : compositeMetadata) {
            if (AUTHENTICATION_MIME_TYPE_VALUE.equals(entry.getMimeType())) {
                ByteBuf content = entry.getContent();
                WellKnownAuthType wellKnownAuthType = AuthMetadataCodec.readWellKnownAuthType(content);
                if (WellKnownAuthType.BEARER.equals(wellKnownAuthType)) {
                    char[] rawToken = AuthMetadataCodec.readBearerTokenAsCharArray(content);
                    String token = new String(rawToken);
                    Long subject = JwtUtils.parseSubject(token);
                    return reactiveCredentialsService.getById(subject)
                            .filter((credentials) -> JwtUtils.isValid(token, credentials))
                            .switchIfEmpty(Mono.error(new BadCredentialsException("Session expired, please login")))
                            .map(user -> new BearerAuthenticationToken(user, token));
                }
            }
        }
        return Mono.empty();
    }
}