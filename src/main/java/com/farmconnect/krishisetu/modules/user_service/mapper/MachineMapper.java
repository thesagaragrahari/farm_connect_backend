package com.farmconnect.krishisetu.modules.user_service.mapper;

import org.mapstruct.Mapper;

import com.farmconnect.krishisetu.modules.user_service.entity.Machine;
import com.farmconnect.krishisetu.modules.user_service.model.MachineProfile;

@Mapper(componentModel = "spring")
public interface MachineMapper {

    MachineProfile toMachineModel(Machine machine);
    
    Machine toMachineEntity(MachineProfile  machineProfile);
}
