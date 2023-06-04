package com.gihub.blackchronos.finny.emitter.allornothing;

import com.gihub.blackchronos.finny.emitter.Emitter;
import lombok.Getter;
import reactor.core.publisher.Flux;

public abstract class MulticastAllOrNothingEmitter<T> extends MulticastAllOrNothing<T> implements Emitter<T> {
    @Getter
    private final Flux<T> flux = this.getSinks().asFlux();
}
