package com.farmconnect.krishisetu.users_management.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.farmconnect.krishisetu.users_management.entity.Farmer;

@Repository
public interface FarmerRepo extends JpaRepository<Farmer, Integer> {
    // Optional: find farmers by userId
    Farmer findByUserId(Integer userId);
}
