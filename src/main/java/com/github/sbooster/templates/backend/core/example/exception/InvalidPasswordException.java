package com.github.sbooster.templates.backend.core.example.exception;

import org.slf4j.helpers.MessageFormatter;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() {
    }

    public InvalidPasswordException(String message, Object... arguments) {
        super(MessageFormatter.arrayFormat(message, arguments).getMessage());
    }
}
