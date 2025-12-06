package com.farmconnect.krishisetu.users_management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.farmconnect.krishisetu.users_management.entity.WorkerSkillMapping;
import com.farmconnect.krishisetu.users_management.model.WorkerSkillProfile;


@Mapper(componentModel = "spring", uses = { WorkerMapper.class, SkillMapper.class })
public interface WorkerSkillMappingMapper {

    @Mapping(target = "workerId", source = "worker.workerId")
    @Mapping(target = "skillId", source = "skill.skillId")
    WorkerSkillProfile toWorkerSkillMappingModel(WorkerSkillMapping workerSkillMapping);

    @Mapping(source = "workerId", target = "worker.workerId")
    @Mapping(source = "skillId", target = "skill.skillId")
    WorkerSkillMapping toWorkerSkillMappingEntity(WorkerSkillProfile workerSkillProfile);
    
}