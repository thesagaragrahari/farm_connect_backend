package com.farmconnect.krishisetu.users_management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.farmconnect.krishisetu.users_management.entity.User;
import com.farmconnect.krishisetu.users_management.model.UserProfile;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "pointdto.longitude", expression = "java(user.getLocation() != null ? user.getLocation().getY() : null)")
    @Mapping(target = "pointdto.latitude", expression = "java(user.getLocation() != null ? user.getLocation().getX() : null)")
    UserProfile toUserModel(User user);
    
    @Mapping(target = "location", ignore = true)
    User toUserEntity(UserProfile userProfile);
}
