package com.github.sbooster.templates.backend.emitter.allornothing;

import lombok.Getter;
import reactor.core.publisher.Sinks;

@Getter
public abstract class MulticastAllOrNothing<T> {
    private final Sinks.Many<T> sinks = Sinks.many().multicast().directAllOrNothing();

    public T emit(T t) {
        this.sinks.tryEmitNext(t);
        return t;
    }
}
