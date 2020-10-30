package com.o4.microservices.common.security.config;

import java.security.KeyPair;

public class AbstractSecurityConfig {
    public static final int SECURITY_ERROR_EMPTY_TOKEN = 40001;
    public static final int SECURITY_ERROR_MALFORMED_TOKEN = 40002;
    public static final int SECURITY_ERROR_EXPIRED_TOKEN = 40003;
    public static final int SECURITY_ERROR_TOKEN_UNMARSHAL_EXCEPTION = 40004;
    public static final int SECURITY_ERROR_BAD_CREDENTIALS = 40005;
    public static final int SECURITY_ERROR_INVALID_CREDENTIALS_SCHEME = 40006;
    public static KeyPair keypair = null;

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_SECURITY_TOKEN = "Authorization";
    public static final String URL_AUTH = "/auth/login";
    public static final String URL_AUTH_COOKIE = "/auth/cookie";

    public void addRole() {

    }

}
