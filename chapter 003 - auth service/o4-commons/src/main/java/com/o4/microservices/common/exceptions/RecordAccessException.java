package com.o4.microservices.common.exceptions;

public class RecordAccessException extends ApplicationException {

    public RecordAccessException(String message) {
        this(message, null);
    }

    public RecordAccessException(String message, Throwable cause) {
        this(ERROR_RECORD_NOT_ACCESSIBLE, message, cause);
    }

    public RecordAccessException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }
}