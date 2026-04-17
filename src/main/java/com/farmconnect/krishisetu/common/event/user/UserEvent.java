package com.farmconnect.krishisetu.common.event.user;


import com.farmconnect.krishisetu.common.event.base.BaseEvent;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class UserEvent extends BaseEvent {

    private UserEventType eventType;

    private Long userId;

    private String email;

    private String name;

    private String role;

    private String token;

}