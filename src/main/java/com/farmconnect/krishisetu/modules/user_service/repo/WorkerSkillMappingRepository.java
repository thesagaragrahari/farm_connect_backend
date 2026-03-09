package com.farmconnect.krishisetu.modules.user_service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.farmconnect.krishisetu.modules.user_service.entity.WorkerSkillMapping;

import java.util.List;

@Repository
public interface WorkerSkillMappingRepository extends JpaRepository<WorkerSkillMapping, Integer> {
    
}
