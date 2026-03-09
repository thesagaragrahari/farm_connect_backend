package com.farmconnect.krishisetu.modules.notification_service.event;


import java.io.Serializable;
import java.util.Map;

import com.farmconnect.krishisetu.modules.notification_service.models.NotificationType;

import lombok.Data;

@Data
public class NotificationEvent implements Serializable {

    private String recipient;   // email or future phone
    private NotificationType type;
    private Map<String, String> metadata;
}