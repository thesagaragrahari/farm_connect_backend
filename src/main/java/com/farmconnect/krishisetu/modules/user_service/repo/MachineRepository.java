package com.farmconnect.krishisetu.modules.user_service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.farmconnect.krishisetu.modules.user_service.entity.Machine;

import java.util.List;

@Repository
public interface MachineRepository extends JpaRepository<Machine, Integer> {
    List<Machine> findBySkillId(Integer skillId);
}
