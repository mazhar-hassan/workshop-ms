package com.ptv.livebox.authentication.service;

import com.ptv.livebox.authentication.repository.UserEntity;
import com.ptv.livebox.authentication.repository.UserRepository;
import com.ptv.livebox.authentication.repository.UserRoleEntity;
import com.ptv.livebox.authentication.repository.UserRoleRepository;
import com.ptv.livebox.security.common.data.AuthenticatedUser;
import com.ptv.livebox.security.common.data.AutheticatedUserRole;
import com.ptv.livebox.security.common.provider.UserAuthenticationService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserAuthenticationServiceImpl implements UserAuthenticationService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    public UserAuthenticationServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository) {
        super();
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    public AuthenticatedUser authenticate(String username, String password) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username);
        if (null == user) {
            throw new UsernameNotFoundException("Cannot find username:" + username);
        }

        if (!match(user.getPassword(), password)) {
            throw new BadCredentialsException("Invalid credentials provided");
        }

        List<UserRoleEntity> roles = userRoleRepository.findByUsername(username);

        return toAuthenticatedUser(user, roles);
    }

    private AuthenticatedUser toAuthenticatedUser(UserEntity user, List<UserRoleEntity> roles) {
        AuthenticatedUser session = new AuthenticatedUser();
        session.setId(user.getId());
        session.setUsername(user.getUsername());
        session.setRoles(new ArrayList<>());

        session.setRoles(roles.stream()
                .map(this::toAuthRole)
                .collect(Collectors.toList()));

        return session;
    }

    private AutheticatedUserRole toAuthRole(UserRoleEntity entity) {
        AutheticatedUserRole role = new AutheticatedUserRole();
        role.setId(entity.getId());
        role.setRoleName(entity.getRoleName());

        return role;
    }


    private boolean match(String bcryptPassword, String plainPassword) {
        return new BCryptPasswordEncoder(12).matches(plainPassword, bcryptPassword);
    }
}
