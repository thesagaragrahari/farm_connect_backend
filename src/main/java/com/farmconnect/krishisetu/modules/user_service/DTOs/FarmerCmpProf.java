package com.farmconnect.krishisetu.modules.user_service.DTOs;

import com.farmconnect.krishisetu.modules.user_service.model.PointDTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Getter
@Setter
public class FarmerCmpProf {
    private String phone;
    private String landArea;
    private PointDTO location;
}
