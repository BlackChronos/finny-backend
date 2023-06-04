package com.gihub.blackchronos.finny.emitter.allornothing;

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
