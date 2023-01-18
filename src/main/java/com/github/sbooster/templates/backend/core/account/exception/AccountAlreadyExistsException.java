package com.github.sbooster.templates.backend.core.account.exception;


import com.github.sbooster.templates.backend.exception.AbstractMonoException;

public class AccountAlreadyExistsException extends AbstractMonoException {
    public AccountAlreadyExistsException(String message, Object... arguments) {
        super(message, arguments);
    }
}
