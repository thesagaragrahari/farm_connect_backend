package com.farmconnect.krishisetu.users_management.mapper;

import org.mapstruct.Mapper;

import com.farmconnect.krishisetu.users_management.entity.Worker;
import com.farmconnect.krishisetu.users_management.model.WorkerProfile;



@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface WorkerMapper {
    WorkerProfile toWorkerModel(Worker worker);
    Worker toFarmerEntity(WorkerProfile workerProfile);
}
