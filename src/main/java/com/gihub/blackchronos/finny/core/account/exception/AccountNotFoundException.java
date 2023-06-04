package com.gihub.blackchronos.finny.core.account.exception;


import com.gihub.blackchronos.finny.exception.AbstractMonoException;

public class AccountNotFoundException extends AbstractMonoException {
    public AccountNotFoundException(String message, Object... arguments) {
        super(message, arguments);
    }
}
