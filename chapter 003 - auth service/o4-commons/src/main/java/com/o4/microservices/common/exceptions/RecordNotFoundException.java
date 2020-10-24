package com.o4.microservices.common.exceptions;

public class RecordNotFoundException extends ApplicationException {
    public RecordNotFoundException(String message) {
        this(message, null);
    }

    public RecordNotFoundException(String message, Throwable cause) {
        super(ERROR_RECORD_NOT_FOUND, message, cause);
    }
}