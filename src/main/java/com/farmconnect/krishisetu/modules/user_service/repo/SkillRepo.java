package com.farmconnect.krishisetu.modules.user_service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.farmconnect.krishisetu.modules.user_service.entity.Skill;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkillRepo extends JpaRepository<Skill, Integer> {
    
    // Find skill by name
    Optional<Skill> findBySkillName(String skillName);

    List<Skill> findBySkillIdIn(List<Integer> skillIds);
    // Check if a skill exists by name
    boolean existsBySkillName(String skillName);

    List<Skill> findBySkillNameIn(List<String> skillReq);

}
