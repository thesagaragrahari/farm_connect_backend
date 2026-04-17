package com.farmconnect.krishisetu.modules.auth_service.service;


import com.farmconnect.krishisetu.security.facade.*;
import com.farmconnect.krishisetu.modules.user_service.repo.UserRepo;
import com.farmconnect.krishisetu.modules.auth_service.entities.UserSecurityState;
import com.farmconnect.krishisetu.modules.auth_service.repositories.UserSecurityStateRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserSecurityFacadeImpl implements UserSecurityFacade {

    private final UserRepo userRepo;
    private final UserSecurityStateRepository stateRepo;

    public UserSecurityFacadeImpl(UserRepo userRepo,
                                  UserSecurityStateRepository stateRepo) {
        this.userRepo = userRepo;
        this.stateRepo = stateRepo;
    }

    @Override
    public Optional<SecurityUserDto> findByEmail(String email) {

        return userRepo.findByEmail(email)
                .map(user -> {

                    UserSecurityState state =
                            stateRepo.findById(user.getUserId())
                                    .orElseThrow();

                    return new SecurityUserDto(
                            user.getUserId(),
                            user.getEmail(),
                            user.getPassword(),
                            user.getRole(),
                            state.isEmailVerified(),
                            state.isAccountLocked(),
                            state.isProfileCompleted()
                    );
                });
    }
}
