package com.ptv.livebox.movie.common.exceptions;

public class ExceptionResponse {
    private int code;
    private String message;

    public static ExceptionResponse of(ApplicationException exception) {
        return of(exception.getErrorCode(), exception.getMessage());
    }

    public static ExceptionResponse of(int code, String message) {
        ExceptionResponse response = new ExceptionResponse();
        response.setCode(code);
        response.setMessage(message);

        return response;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}