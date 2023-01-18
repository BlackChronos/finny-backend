package com.github.sbooster.templates.backend.emitter;

import reactor.core.publisher.Flux;

public interface Emitter<T> {
    T emit(T t);

    Flux<T> getFlux();
}
