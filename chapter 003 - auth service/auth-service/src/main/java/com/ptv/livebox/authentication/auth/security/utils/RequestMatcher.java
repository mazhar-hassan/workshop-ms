package com.ptv.livebox.authentication.auth.security.utils;

import com.ptv.livebox.authentication.config.ApplicationSecurityConfiguration;

import javax.servlet.http.HttpServletRequest;

public class RequestMatcher {

    public static boolean isAuthRequest(HttpServletRequest httpRequest, String path) {
        return ApplicationSecurityConfiguration.URL_AUTH.equalsIgnoreCase(path) && httpRequest.getMethod().equals("POST");
    }

    public static boolean isCookieAuthRequest(HttpServletRequest httpRequest, String path) {
        return ApplicationSecurityConfiguration.URL_AUTH_COOKIE.equalsIgnoreCase(path) && httpRequest.getMethod().equals("POST");
    }
}
