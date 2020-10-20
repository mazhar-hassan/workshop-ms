package com.ptv.livebox.authentication.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PTVUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    public PTVUserDetailService(UserRepository userRepository, UserRoleRepository userRoleRepository) {
        super();
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username);
        if (null == user) {
            throw new UsernameNotFoundException("Cannot find username:" + username);
        }
        List<UserRoleEntity> roles = userRoleRepository.findByUsername(username);

        return new PTVUserDetails(user, roles);
    }
}
