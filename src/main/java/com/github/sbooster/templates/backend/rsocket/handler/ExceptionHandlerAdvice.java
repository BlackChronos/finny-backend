package com.github.sbooster.templates.backend.rsocket.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerAdvice {
    @ExceptionHandler
    public ResponseEntity<?> handleException(Throwable throwable) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(throwable.getMessage());
    }

    @MessageExceptionHandler({Throwable.class})
    public Mono<?> handleMessageException(Throwable throwable) {
        return Mono.just(throwable);
    }
}