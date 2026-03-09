package com.farmconnect.krishisetu.modules.user_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.farmconnect.krishisetu.modules.user_service.entity.User;
import com.farmconnect.krishisetu.modules.user_service.model.UserProfile;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "pointdto.longitude", expression = "java(user.getLocation() != null ? user.getLocation().getY() : null)")
    @Mapping(target = "pointdto.latitude", expression = "java(user.getLocation() != null ? user.getLocation().getX() : null)")
    UserProfile toUserModel(User user);
    
    @Mapping(target = "location", ignore = true)
    User toUserEntity(UserProfile userProfile);
}
