package com.kshrd.krorya.service;

import com.kshrd.krorya.model.dto.NotificationDTO;
import com.kshrd.krorya.model.entity.Notification;
import com.kshrd.krorya.model.request.NotificationRequest;

import java.util.List;

public interface NotificationService {
    Notification saveNotification(NotificationRequest notification);
    List<Notification> getNotifications();
}
