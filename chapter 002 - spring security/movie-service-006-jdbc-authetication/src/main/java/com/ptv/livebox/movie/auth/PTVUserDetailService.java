package com.ptv.livebox.movie.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PTVUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    public PTVUserDetailService(UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username);
        if (null == user) {
            throw new UsernameNotFoundException("Cannot find username:" + username);
        }

        return new PTVUserDetails(user);
    }
}
