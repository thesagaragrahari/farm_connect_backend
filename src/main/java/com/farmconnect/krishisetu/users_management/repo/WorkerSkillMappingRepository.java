package com.farmconnect.krishisetu.users_management.repo;

import com.farmconnect.krishisetu.users_management.entity.WorkerSkillMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkerSkillMappingRepository extends JpaRepository<WorkerSkillMapping, Integer> {

    @Query("select wsm.skillId from WorkerSkillMapping wsm where wsm.workerId = ?1")
    List<Integer> findByWorkerId(Integer workerId);

    @Query("select wsm from WorkerSkillMapping wsm where wsm.skillId = ?1")
    List<WorkerSkillMapping> findBySkillId(Integer skillId);
}
