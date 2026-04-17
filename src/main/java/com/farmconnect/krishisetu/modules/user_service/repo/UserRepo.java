package com.farmconnect.krishisetu.modules.user_service.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.farmconnect.krishisetu.modules.user_service.entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {



    boolean existsByEmail(String email);

    // Optional: custom query methods can be added here
    Optional<User> findByEmail(String email);
    //User findByEmail(String email);
    Optional<User> findByUserId(Long userId);
    Optional<User>findByUserIdAndRole(Long userId, String role);
    
}
