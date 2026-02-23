package com.github.gianlucampos.springbootinvestsync.exception;

public class HoldingsProviderException extends RuntimeException {

    public HoldingsProviderException(String message) {
        super(message);
    }
    public HoldingsProviderException(String message, Throwable cause) {
        super(message, cause);
    }
}
