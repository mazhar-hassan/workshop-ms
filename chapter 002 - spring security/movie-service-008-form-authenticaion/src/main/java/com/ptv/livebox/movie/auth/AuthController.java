package com.ptv.livebox.movie.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String getLoginForm() {
        return "loginform";
    }

    @GetMapping(value = "/logout-success")
    public String getLogoutPage() {
        return "logout-success";
    }
}
