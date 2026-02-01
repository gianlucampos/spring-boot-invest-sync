package com.github.gianlucampos.springbootinvestsync.exception;

public class SheetsException extends RuntimeException{

    public SheetsException(Exception e) {
        super(e);
    }

    public SheetsException(String message) {
        super(message);
    }

    public SheetsException(String message, Throwable cause) {
        super(message, cause);
    }
}
