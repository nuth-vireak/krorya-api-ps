package com.kshrd.krorya.controller;

import com.kshrd.krorya.model.dto.NotificationDTO;
import com.kshrd.krorya.model.entity.AppUser;
import com.kshrd.krorya.model.entity.CustomUserDetail;
import com.kshrd.krorya.model.entity.Notification;
import com.kshrd.krorya.model.request.NotificationRequest;
import com.kshrd.krorya.model.response.ApiResponse;
import com.kshrd.krorya.service.NotificationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/vi/notification")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<ApiResponse<Notification>> createNotification(@Valid @RequestBody NotificationRequest notification) {

        return ResponseEntity.ok(ApiResponse.<Notification>builder()
                .message("Successfully created a new notification")
                .code(200)
                .payload(notificationService.saveNotification(notification))
                .status(HttpStatus.CREATED)
                .localDateTime(LocalDateTime.now())
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Notification>>> getAllNotifications() {
        List<Notification> notifications = notificationService.getNotifications();
        return ResponseEntity.ok(ApiResponse.<List<Notification>>builder()
                .message("Get all notifications successfully")
                .code(200)
                .status(HttpStatus.OK)
                .payload(notifications)
                .localDateTime(LocalDateTime.now())
                .build());
    }

}
