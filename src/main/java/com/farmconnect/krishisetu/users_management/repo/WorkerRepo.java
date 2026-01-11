package com.farmconnect.krishisetu.users_management.repo;


import com.farmconnect.krishisetu.users_management.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkerRepo extends JpaRepository<Worker, Integer> {
    List<Worker> findByStatus(String status);
    List<Worker> findByJobType(String jobType);
    Worker findByUserUserId(Long userId);
    @Query("""
    SELECT w
    FROM Worker w
    JOIN w.user u
    WHERE u.email = :email
    """)
    Optional<Worker> findByUserEmail(@Param("email") String email);


   @Query(value = """
    SELECT DISTINCT w.*
    FROM users.workers w
    JOIN users.worker_skill_mapping wm 
        ON w.worker_id = wm.worker_id
    WHERE wm.skill_id IN (:skillIds)
    """, nativeQuery = true)
    List<Worker> findWorkersHavingAnySkill(@Param("skillIds") List<Integer> skillIds);
    //Worker findByEmail(String email);

    //List<Worker> findBySkills(List<Skill> validSkills);
}
