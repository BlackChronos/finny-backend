package com.github.sbooster.templates.backend.emitter.besteffort;

import lombok.Getter;
import reactor.core.publisher.Sinks;

@Getter
public abstract class MulticastBestEffort<T> {
    private final Sinks.Many<T> sinks = Sinks.many().multicast().directBestEffort();

    public T emit(T t) {
        this.sinks.tryEmitNext(t);
        return t;
    }
}
