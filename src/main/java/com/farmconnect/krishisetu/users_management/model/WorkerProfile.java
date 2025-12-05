package com.farmconnect.krishisetu.users_management.model;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;


@Data
public class WorkerProfile {
    
    UserProfile userProfile;
    private List<String> skill_set;
    private String status;
    private BigDecimal daily_wage;
    private Integer experience_years;
    private String job_type;
    private String machine_id;
    //    worker_id, user_id, daily_wage, experience_years, status, job_type, machine_id, created_at, updated_at
}
