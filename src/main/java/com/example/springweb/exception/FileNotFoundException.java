package com.example.springweb.exception;

public class FileNotFoundException extends RuntimeException {

    private static final String ERROR_MESSAGE = "File %s does not exist";

    public FileNotFoundException(String fileName) {
        super(String.format(ERROR_MESSAGE, fileName));
    }
}
