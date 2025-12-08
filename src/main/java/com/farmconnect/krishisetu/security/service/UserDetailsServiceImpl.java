package com.farmconnect.krishisetu.security.service;

import com.farmconnect.krishisetu.users_management.entity.User;
import com.farmconnect.krishisetu.users_management.repo.UserRepo;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepo userRepo;

    public UserDetailsServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Assume you have a findByEmail method in your UserRepo
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));

        // Note: You need to create a custom UserDetails implementation if your User entity 
        // doesn't directly implement UserDetails. A simple way is to use Spring's User class.
         SimpleGrantedAuthority authority =
        new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase());
        
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(authority)
                // Assign appropriate roles/authorities based on user.getUserType()
                // Example: List.of(new SimpleGrantedAuthority("ROLE_" + user.getUserType().toUpperCase()))
        // Assumes your User entity has a getAuthorities method
        );
    }


    
}