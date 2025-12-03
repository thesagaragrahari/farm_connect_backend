package com.farmconnect.krishisetu.users_management.repo;

import com.farmconnect.krishisetu.users_management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    // Optional: custom query methods can be added here
    User findByEmail(String email);
}
