package com.farmconnect.krishisetu.users_management.mapper;

import org.mapstruct.Mapper;

import com.farmconnect.krishisetu.users_management.entity.User;
import com.farmconnect.krishisetu.users_management.model.UserProfile;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserProfile toUserModel(User user);
    
    User toUserEntity(UserProfile userProfile);
}
