package com.farmconnect.krishisetu.modules.user_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkerSkillProfile {
    private Integer id;
    private Integer workerId;
    private Integer skillId;
}
