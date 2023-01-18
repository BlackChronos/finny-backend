package com.github.sbooster.templates.backend.core.account.exception;


import com.github.sbooster.templates.backend.exception.AbstractMonoException;

public class AccountNotFoundException extends AbstractMonoException {
    public AccountNotFoundException(String message, Object... arguments) {
        super(message, arguments);
    }
}
