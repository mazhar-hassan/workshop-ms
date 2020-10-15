package com.ptv.livebox.movie.common.exceptions;

public class MissingRequiredFieldException  extends ApplicationException {

    public MissingRequiredFieldException(String message) {
        this(message, null);
    }

    public MissingRequiredFieldException(String message, Throwable cause) {
        this(ERROR_REQUIRED_FIELD_MISSING, message, cause);
    }

    public MissingRequiredFieldException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
