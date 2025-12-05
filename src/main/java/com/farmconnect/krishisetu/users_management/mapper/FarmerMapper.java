package com.farmconnect.krishisetu.users_management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.farmconnect.krishisetu.users_management.entity.Farmer;
import com.farmconnect.krishisetu.users_management.model.FarmerProfile;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface FarmerMapper {

    @Mapping(source = "user", target = "userProfile")  // nested via UserMapper
    @Mapping(source = "landAreaAcre", target = "landArea")
    FarmerProfile toFarmerModel(Farmer farmer);

    @Mapping(source = "userProfile", target = "user")
    @Mapping(source = "landArea", target = "landAreaAcre")
    Farmer toFarmerEntity(FarmerProfile farmerProfile);
}

