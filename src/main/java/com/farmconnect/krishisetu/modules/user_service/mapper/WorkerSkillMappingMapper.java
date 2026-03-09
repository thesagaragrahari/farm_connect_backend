package com.farmconnect.krishisetu.modules.user_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.farmconnect.krishisetu.modules.user_service.entity.WorkerSkillMapping;
import com.farmconnect.krishisetu.modules.user_service.model.WorkerSkillProfile;


@Mapper(componentModel = "spring", uses = { WorkerMapper.class, SkillMapper.class })
public interface WorkerSkillMappingMapper {

    @Mapping(target = "workerId", source = "worker.workerId")
    @Mapping(target = "skillId", source = "skill.skillId")
    WorkerSkillProfile toWorkerSkillMappingModel(WorkerSkillMapping workerSkillMapping);

    @Mapping(source = "workerId", target = "worker.workerId")
    @Mapping(source = "skillId", target = "skill.skillId")
    WorkerSkillMapping toWorkerSkillMappingEntity(WorkerSkillProfile workerSkillProfile);
    
}