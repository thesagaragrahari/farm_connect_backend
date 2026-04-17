package com.farmconnect.krishisetu.common.event.job;


import com.farmconnect.krishisetu.common.event.base.BaseEvent;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobEvent extends BaseEvent {

    private JobEventType eventType;

    private Long jobId;

    private Long workerId;

}