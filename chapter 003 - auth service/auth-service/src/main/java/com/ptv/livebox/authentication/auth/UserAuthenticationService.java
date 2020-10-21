package com.ptv.livebox.authentication.auth;

import com.ptv.livebox.authentication.auth.security.SessionRole;
import com.ptv.livebox.authentication.auth.security.ApplicationSession;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserAuthenticationService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    public UserAuthenticationService(UserRepository userRepository, UserRoleRepository userRoleRepository) {
        super();
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    public ApplicationSession authenticate(String username, String password) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username);
        if (null == user) {
            throw new UsernameNotFoundException("Cannot find username:" + username);
        }

        if (!match(user.getPassword(), password)) {
            throw new BadCredentialsException("Invalid credentials provided");
        }

        List<UserRoleEntity> roles = userRoleRepository.findByUsername(username);

        return toUserSession(user, roles);
    }

    private ApplicationSession toUserSession(UserEntity user, List<UserRoleEntity> roles) {
        ApplicationSession session = new ApplicationSession();
        session.setId(user.getId());
        session.setUsername(user.getUsername());
        session.setRoles(new ArrayList<>());

        session.setRoles(roles.stream()
                .map(this::toSessionRole)
                .collect(Collectors.toList()));

        return session;
    }

    private SessionRole toSessionRole(UserRoleEntity entity) {
        SessionRole role = new SessionRole();
        role.setId(entity.getId());
        role.setRoleName(entity.getRoleName());

        return role;
    }


    private boolean match(String bcryptPassword, String plainPassword) {
        return new BCryptPasswordEncoder(12).matches(plainPassword, bcryptPassword);
    }
}
