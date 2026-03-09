package com.farmconnect.krishisetu.modules.user_service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.farmconnect.krishisetu.modules.user_service.entity.Farmer;
import com.farmconnect.krishisetu.modules.user_service.entity.User;

@Repository
public interface FarmerRepo extends JpaRepository<Farmer, Integer> {
    // Optional: find farmers by userId
    
    Farmer findByUserUserId(Long userId);

    Farmer findByUserEmail(String email);

    boolean existsByUser(User user);
}
