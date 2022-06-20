package com.thiago.minhasfinancas.exception;

public class BusinessRuleException extends RuntimeException {
    public BusinessRuleException(String msg) {
        super(msg);
    }
}
