package com.farmconnect.krishisetu.users_management.repo;

import com.farmconnect.krishisetu.users_management.entity.WorkerSkillMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkerSkillMappingRepository extends JpaRepository<WorkerSkillMapping, Integer> {
    List<WorkerSkillMapping> findByWorkerId(Integer workerId);
    List<WorkerSkillMapping> findBySkillId(Integer skillId);
}
