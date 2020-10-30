package com.ptv.livebox.security.common.provider;

import com.ptv.livebox.security.common.data.AuthenticatedUser;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserAuthenticationService {
    AuthenticatedUser authenticate(String username, String password) throws UsernameNotFoundException;
}
