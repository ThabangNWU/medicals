package com.thogwa.thogwa.backend.exception;

public class ProductNotExistsException extends IllegalArgumentException{
    public ProductNotExistsException(String msg) {
        super(msg);
    }
}
