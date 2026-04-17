package com.farmconnect.krishisetu.common.event.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public abstract class BaseEvent {

    private LocalDateTime createdAt =
            LocalDateTime.now();

}
