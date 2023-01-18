package com.github.sbooster.templates.backend.rsocket.security.bearer;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.sbooster.templates.backend.rsocket.security.model.StoredUserDetails;
import com.github.sbooster.templates.backend.rsocket.security.service.ReactiveCredentialsService;
import io.netty.buffer.ByteBuf;
import io.rsocket.metadata.AuthMetadataCodec;
import io.rsocket.metadata.CompositeMetadata;
import io.rsocket.metadata.WellKnownAuthType;
import io.rsocket.metadata.WellKnownMimeType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.rsocket.api.PayloadExchange;
import org.springframework.security.rsocket.authentication.PayloadExchangeAuthenticationConverter;
import reactor.core.publisher.Mono;

public record BearerPayloadExchangeConverter(ReactiveCredentialsService<?> reactiveCredentialsService,
                                             Algorithm algorithm) implements PayloadExchangeAuthenticationConverter {
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
                    DecodedJWT decodedJWT = JWT.decode(token);
                    Long subject = Long.valueOf(decodedJWT.getSubject());
                    return this.reactiveCredentialsService.getById(subject)
                            .filter(credentials -> this.isValid(decodedJWT, credentials))
                            .switchIfEmpty(Mono.error(() ->
                                    new BadCredentialsException("Session expired, please login")
                            ))
                            .map(user -> new BearerAuthenticationToken(user, token));
                }
            }
        }
        return Mono.empty();
    }

    public boolean isValid(DecodedJWT token, StoredUserDetails userDetails) {
        try {
            JWT.require(this.algorithm)
                    .withSubject(String.valueOf(userDetails.getId()))
                    .withClaim("ema", userDetails.getUsername())
                    .withClaim("pwd", userDetails.getPassword())
                    .withArrayClaim("typ", userDetails.getAuthorities()
                            .stream()
                            .map(GrantedAuthority::getAuthority)
                            .toArray(String[]::new)
                    )
                    .build().verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }
}
