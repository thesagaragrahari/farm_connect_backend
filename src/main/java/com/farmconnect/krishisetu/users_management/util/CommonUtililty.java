package com.farmconnect.krishisetu.users_management.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.farmconnect.krishisetu.users_management.entity.Skill;
import com.farmconnect.krishisetu.users_management.repo.SkillRepo;
import com.farmconnect.krishisetu.users_management.repo.WorkerSkillMappingRepository;

@Component
public class CommonUtililty {

    @Autowired
    WorkerSkillMappingRepository workerSkillMappingRepo;

    @Autowired
    SkillRepo skillRepo;

    public List<Skill> populateWorkerSkills(Integer userId){
            
        List<Integer> skillIds = workerSkillMappingRepo.findByWorkerId(userId);
        if(skillIds.isEmpty()) {
            return List.of();
        }
        List<Skill> skills = skillRepo.findBySkillIdIn(skillIds); 
        if(skills.isEmpty() ) {
            return List.of();
        }
        
        
        return skills;
    }

}
