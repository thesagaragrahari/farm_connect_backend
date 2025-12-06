package com.farmconnect.krishisetu.users_management.repo;


import com.farmconnect.krishisetu.users_management.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkerRepo extends JpaRepository<Worker, Integer> {
    List<Worker> findByStatus(String status);
    List<Worker> findByJobType(String jobType);
    
    Worker findByUserUserId(Long userId);
}
