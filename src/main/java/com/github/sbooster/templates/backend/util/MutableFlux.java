package com.github.sbooster.templates.backend.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@RequiredArgsConstructor
public class MutableFlux<T> {
    private final Sinks.Many<T> emitter = Sinks.many().multicast().directBestEffort();
    @Getter
    private final Flux<T> flux = emitter.asFlux().share();

    public T add(T value) {
        emitter.tryEmitNext(value);
        return value;
    }
}
