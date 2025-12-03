package com.farmconnect.krishisetu.users_management.repo;

import com.farmconnect.krishisetu.users_management.entity.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MachineRepository extends JpaRepository<Machine, Integer> {
    List<Machine> findBySkillId(Integer skillId);
}
