package com.farmconnect.krishisetu.users_management.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.farmconnect.krishisetu.users_management.entity.Skill;
import com.farmconnect.krishisetu.users_management.entity.Worker;
import com.farmconnect.krishisetu.users_management.entity.WorkerSkillMapping;
import com.farmconnect.krishisetu.users_management.model.SkillProfile;
import com.farmconnect.krishisetu.users_management.model.WorkerProfile;
import org.mapstruct.Named; // <-- Needed for named methods, though not strictly required here

@Mapper(componentModel = "spring", uses = { UserMapper.class, MachineMapper.class, SkillMapper.class })
public interface WorkerMapper {

    @Mapping(source = "workerSkillsMappings", target = "skillsProfile")
    @Mapping(source = "user", target = "userProfile")
    @Mapping(source="machine", target = "machineProfile")
    @Mapping(source = "dailyWage", target = "daily_wage")
    @Mapping(source = "experienceYears", target = "experience_years")
    @Mapping(source = "jobType", target = "job_type")
    WorkerProfile toWorkerModel(Worker worker);

    // --- Custom Mapping for Skills List ---

    // MapStruct will automatically use this method to map the List<WorkerSkillMapping>
    // to List<SkillProfile> because it extracts the Skill entity.
    @Mapping(source = "skill", target = ".") // Maps the nested 'skill' object to the target 'SkillProfile'
    SkillProfile map(WorkerSkillMapping workerSkillMapping);

    // --- Entity Mapping ---
    @Mapping(source = "userProfile", target = "user")
    @Mapping(source="machineProfile", target = "machine")
    @Mapping(source = "daily_wage", target = "dailyWage")
    @Mapping(source = "experience_years", target = "experienceYears")
    @Mapping(source = "job_type", target = "jobType")
    @Mapping(target = "workerId", ignore = true) // Ignore auto-generated ID
    @Mapping(target = "workerSkillsMappings", ignore = true) // Ignore the collection for entity creation
    Worker toWorkerEntity(WorkerProfile workerProfile);

    List<WorkerProfile> toWorkerModelList(List<Worker> workers);

    void updateWorkerEntityFromModel(WorkerProfile workerProfile, Worker existingWorker);

    

}