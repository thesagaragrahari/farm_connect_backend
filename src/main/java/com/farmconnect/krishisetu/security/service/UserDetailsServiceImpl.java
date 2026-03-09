package com.farmconnect.krishisetu.security.service;

import com.farmconnect.krishisetu.modules.auth_service.entities.UserSecurityState;
import com.farmconnect.krishisetu.modules.auth_service.repositories.UserSecurityStateRepository;
import com.farmconnect.krishisetu.modules.user_service.entity.User;
import com.farmconnect.krishisetu.modules.user_service.repo.UserRepo;

import java.util.List;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepo userRepo;
    private final UserSecurityStateRepository userSecurityStateRepo;

    public UserDetailsServiceImpl(
            UserRepo userRepo,
            UserSecurityStateRepository userSecurityStateRepo) {

        this.userRepo = userRepo;
        this.userSecurityStateRepo = userSecurityStateRepo;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        // 1. Load user
        User user = userRepo.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found: " + email));

        // 2. Load security state
        UserSecurityState securityState =
                userSecurityStateRepo.findById(user.getUserId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Security state missing for user"));

        // 3. Security checks
        if (!securityState.isEmailVerified()) {
            throw new DisabledException("Email not verified");
        }

        if (securityState.isAccountLocked()) {
            throw new LockedException("Account locked");
        }

        // 4. Role mapping
        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority(
                        "ROLE_" + user.getRole().toUpperCase());

        // 5. Return Spring Security User
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(authority)
        );
    }
}