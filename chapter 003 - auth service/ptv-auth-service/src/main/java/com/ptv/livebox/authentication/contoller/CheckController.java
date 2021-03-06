package com.ptv.livebox.authentication.contoller;

import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/api/admin")
    public String apiAdmin() {
        return "Secure API admin role access";
    }
}
