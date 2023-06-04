package com.gihub.blackchronos.finny.emitter.besteffort;

import com.gihub.blackchronos.finny.emitter.Emitter;
import lombok.Getter;
import reactor.core.publisher.Flux;

public abstract class SharedMulticastBestEffortEmitter<T> extends MulticastBestEffort<T> implements Emitter<T> {
    @Getter
    private final Flux<T> flux = this.getSinks().asFlux().share();
}
