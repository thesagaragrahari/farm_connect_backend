package com.farmconnect.krishisetu.modules.auth_service.event;




import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserRegisteredEvent {

    private final Long userId;
    private final String email;
    private final String name;
}