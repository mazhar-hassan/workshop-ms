package com.ptv.livebox.authentication.auth.security.utils;

import com.ptv.livebox.authentication.auth.security.SecurityData;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

public interface HTTPUtils {

    public static String getHeader(HttpServletRequest request, String key) {
        return request.getHeader(key);
    }

    public static SecurityData getSecurityData(HttpServletRequest request) {

        SecurityData data = new SecurityData();
        if (!isEmpty(request.getHeader("Authorization"))) {
            setAuthorization(request.getHeader("Authorization"), data);
        }

        data.setUsername(request.getParameter("username"));
        data.setPassword(request.getParameter("password"));
        data.setIpAddress(request.getRemoteAddr());

        return data;
    }

    public static boolean isEmpty(String value) {
        return value == null || value.trim().length() == 0;
    }

    static void setAuthorization(String authorizationHeader, SecurityData data) {
        if (authorizationHeader.startsWith("Basic ")) {
            data.setType("credentials");
            String sec = decode(authorizationHeader.replace("Basic ", ""));
            if (sec != null && sec.contains(":")) {
                String[] pair = sec.split(":");
                data.setApiKey(pair[0]);
                data.setApiSecret(pair[1]);
            }

        } else if (authorizationHeader.startsWith("Bearer ")) {
            data.setType("token");
            data.setToken(authorizationHeader.replace("Bearer ", ""));
        }
    }

    static String decode(String encoded) {
        try {
            return new String(Base64.getDecoder().decode(encoded), "utf8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    static String getPath(HttpServletRequest httpRequest) {
        return new UrlPathHelper().getPathWithinApplication(httpRequest);
    }
}
