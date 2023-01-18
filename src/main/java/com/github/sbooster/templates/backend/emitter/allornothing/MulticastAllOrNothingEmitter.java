package com.github.sbooster.templates.backend.emitter.allornothing;

import com.github.sbooster.templates.backend.emitter.Emitter;
import lombok.Getter;
import reactor.core.publisher.Flux;

public abstract class MulticastAllOrNothingEmitter<T> extends MulticastAllOrNothing<T> implements Emitter<T> {
    @Getter
    private final Flux<T> flux = this.getSinks().asFlux();
}
