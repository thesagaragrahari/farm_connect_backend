package com.farmconnect.krishisetu.users_management.repo;

import com.farmconnect.krishisetu.users_management.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkillRepo extends JpaRepository<Skill, Integer> {
    
    // Find skill by name
    Optional<Skill> findBySkillName(String skillName);

    List<Skill> findBySkillIdIn(List<Integer> skillIds);
    // Check if a skill exists by name
    boolean existsBySkillName(String skillName);

}
