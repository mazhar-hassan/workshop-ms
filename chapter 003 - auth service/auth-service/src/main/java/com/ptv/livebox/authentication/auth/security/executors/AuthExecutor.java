package com.ptv.livebox.authentication.auth.security.executors;

import com.ptv.livebox.authentication.auth.security.SecurityData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface AuthExecutor {
    void execute(SecurityData security,
                 HttpServletRequest httpRequest,
                 HttpServletResponse httpResponse) throws IOException;
}
