package com.farmconnect.krishisetu.users_management.mapper;

import org.mapstruct.Mapper;

import com.farmconnect.krishisetu.users_management.entity.Machine;
import com.farmconnect.krishisetu.users_management.model.MachineProfile;

@Mapper(componentModel = "spring")
public interface MachineMapper {

    MachineProfile toMachineModel(Machine machine);
    
    Machine toMachineEntity(MachineProfile  machineProfile);
}
