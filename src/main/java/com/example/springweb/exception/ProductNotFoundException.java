package com.example.springweb.exception;

import java.util.UUID;

public class ProductNotFoundException extends RuntimeException {

    private static final String ERROR_MESSAGE = "Product n°%s does not exist";

    public ProductNotFoundException(UUID id) {
        super(String.format(ERROR_MESSAGE, id));
    }

    public ProductNotFoundException(UUID id, Throwable throwable) {
        super(String.format(ERROR_MESSAGE, id), throwable);
    }
}
