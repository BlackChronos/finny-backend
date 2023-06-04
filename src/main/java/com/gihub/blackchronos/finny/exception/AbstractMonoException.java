package com.gihub.blackchronos.finny.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.helpers.MessageFormatter;
import reactor.core.publisher.Mono;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AbstractMonoException extends RuntimeException {

    public AbstractMonoException(String message, Object... arguments) {
        super(MessageFormatter.arrayFormat(message, arguments).getMessage());
    }

    public <T> Mono<T> toMono() {
        return Mono.error(() -> this);
    }
}
