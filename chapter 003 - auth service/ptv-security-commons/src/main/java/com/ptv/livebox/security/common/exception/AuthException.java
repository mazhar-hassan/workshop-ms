package com.ptv.livebox.security.common.exception;

public class AuthException extends RuntimeException{
    private int code;

    public AuthException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
