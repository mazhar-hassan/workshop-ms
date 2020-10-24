package com.ptv.livebox.movie.common.exceptions;

public class ApplicationException extends RuntimeException {

    public static final int ERROR_UNKNOWN_ERROR = 1000;
    public static final int ERROR_RECORD_NOT_FOUND = 1001;
    public static final int ERROR_REQUIRED_FIELD_MISSING = 1002;
    public static final int ERROR_ENDPOINT_NOT_FOUND = 1003;
    public static final int ERROR_HTTP_METHOD_NOT_SUPPORTED = 1004;
    public static final int ERROR_RECORD_NOT_ACCESSIBLE = 1006;
    public static final int ERROR_UNABLE_TO_MAP_OBJECT = 1007;
    public static final int ERROR_REFLECTION_FAILED_TO_ACCESS_SETTER = 1008;
    public static final int ERROR_ACCESS_DENIED = 1009;
    public static final int ERROR_ACCESS_FORBIDDEN = 1010;


    private final int errorCode;

    public ApplicationException(int errorCode, String message) {
        this(errorCode, message, null);
    }

    public ApplicationException(int errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}