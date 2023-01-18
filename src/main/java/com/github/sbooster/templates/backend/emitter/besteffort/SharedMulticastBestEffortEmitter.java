package com.github.sbooster.templates.backend.emitter.besteffort;

import com.github.sbooster.templates.backend.emitter.Emitter;
import lombok.Getter;
import reactor.core.publisher.Flux;

public abstract class SharedMulticastBestEffortEmitter<T> extends MulticastBestEffort<T> implements Emitter<T> {
    @Getter
    private final Flux<T> flux = this.getSinks().asFlux().share();
}
