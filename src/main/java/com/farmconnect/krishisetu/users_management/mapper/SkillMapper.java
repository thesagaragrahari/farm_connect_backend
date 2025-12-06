package com.farmconnect.krishisetu.users_management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping; // <-- Import Mapping
import org.mapstruct.Named;

import com.farmconnect.krishisetu.users_management.entity.Skill;
import com.farmconnect.krishisetu.users_management.model.SkillProfile;



@Mapper(componentModel = "spring")
public interface SkillMapper {
    
    @Named("toSkillModel")
    //@Mapping(target = "workerSkillsMappings", ignore = true) // <-- Added to suppress warning
    SkillProfile toSkillModel(Skill skill);
    
    Skill toSkillEntity(SkillProfile skillProfile);
}