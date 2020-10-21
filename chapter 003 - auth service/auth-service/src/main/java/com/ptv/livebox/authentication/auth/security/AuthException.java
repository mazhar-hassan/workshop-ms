package com.ptv.livebox.authentication.auth.security;

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
