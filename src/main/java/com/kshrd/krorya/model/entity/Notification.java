package com.kshrd.krorya.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    @Schema(description = "Unique identifier for the notification", example = "123e4567-e89b-12d3-a456-426614174001" )
    private UUID notificationId;

    @Schema(description = "Title of the notification", example = "New Message Notification" )
    @NotBlank(message = "Title is mandatory")
    @Size(max = 200, message = "Title must be less than 200 characters")
    private String notificationTitle;

    @Schema(description = "Message content of the notification", example = "You have a new message." )
    @NotBlank(message = "Message is mandatory")
    private String notificationMessage;

    @Schema(description = "Type of the notification (e.g., user, recipe)", example = "user")
    @NotBlank(message = "Type is mandatory")
    private String notificationType;

    @Schema(description = "Date and time when the notification was created")
    private Date date;

    @Schema(description = " identifier of the sender who initiated the notification")
    private AppUser sender;

    @Schema(description = " identifier of the recipient who receives the notification", example = "123e4567-e89b-12d3-a456-426614174002" )
    @NotBlank(message = "Recipient ID is mandatory")
    private AppUser recipient;
}
