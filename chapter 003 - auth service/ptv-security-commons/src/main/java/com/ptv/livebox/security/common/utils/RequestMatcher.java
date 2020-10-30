package com.ptv.livebox.security.common.utils;

import com.ptv.livebox.security.common.config.AbstractSecurityConfig;

import javax.servlet.http.HttpServletRequest;

public class RequestMatcher {

    public static boolean isAuthRequest(HttpServletRequest httpRequest, String path) {
        return AbstractSecurityConfig.URL_AUTH.equalsIgnoreCase(path) && httpRequest.getMethod().equals("POST");
    }

    public static boolean isCookieAuthRequest(HttpServletRequest httpRequest, String path) {
        return AbstractSecurityConfig.URL_AUTH_COOKIE.equalsIgnoreCase(path) && httpRequest.getMethod().equals("POST");
    }
}
