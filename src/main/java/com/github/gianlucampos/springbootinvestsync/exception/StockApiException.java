package com.github.gianlucampos.springbootinvestsync.exception;

public class StockApiException extends RuntimeException{

    public StockApiException(Exception e) {
        super(e);
    }
}
