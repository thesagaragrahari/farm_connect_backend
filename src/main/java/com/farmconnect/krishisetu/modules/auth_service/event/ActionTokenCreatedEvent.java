package com.farmconnect.krishisetu.modules.auth_service.event;


import com.farmconnect.krishisetu.modules.auth_service.models.TokenType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ActionTokenCreatedEvent {

    private final String email;
    private final String token;
    private final TokenType tokenType;
}
