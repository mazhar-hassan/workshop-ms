package com.ptv.livebox.authentication;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckController {

    @GetMapping
    public String index() {
        return "Rest controller is working";
    }

    @GetMapping("/api/secure")
    public String apiSecure() {
        return "Secure API access";
    }
}
