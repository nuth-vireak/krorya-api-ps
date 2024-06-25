package com.kshrd.krorya.repository;

import com.kshrd.krorya.model.dto.NotificationDTO;
import com.kshrd.krorya.model.entity.Notification;
import com.kshrd.krorya.model.request.NotificationRequest;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.UUID;

@Mapper
public interface NotificationRepository {
    @Select("""
            INSERT INTO notifications (senderid,recipientid,description,title,type) VALUES (#{senderId}, #{notification.recipientId},#{notification.notificationMessage},#{notification.notificationTitle},#{notification.notificationType}) RETURNING *
            """)
    @Results(id = "notificationMapper", value = {
            @Result(property = "notificationId", column = "notification_id"),
            @Result(property = "notificationTitle", column = "title"),
            @Result(property = "notificationMessage", column = "description"),
            @Result(property = "notificationType", column = "type"),
            @Result(property = "sender",column = "senderid",one = @One(select = "com.kshrd.krorya.repository.AppUserRepository.getUserById")),
            @Result(property = "recipient",column = "recipientid",one = @One(select = "com.kshrd.krorya.repository.AppUserRepository.getUserById"))
    })
    Notification addNotification(@Param("notification") NotificationRequest notification, UUID senderId);

    @Select("""
            SELECT * FROM notifications
            """)
    @ResultMap("notificationMapper")
    List<Notification> getNotifications();
}
