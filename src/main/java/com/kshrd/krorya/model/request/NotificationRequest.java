package com.kshrd.krorya.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {

    @Schema(description = "Title of the notification", example = "New Message Notification", required = true)
    @NotBlank(message = "Title is mandatory")
    @Size(max = 200, message = "Title must be less than 200 characters")
    private String notificationTitle;

    @Schema(description = "Message content of the notification", example = "You have a new message.", required = true)
    @NotBlank(message = "Message is mandatory")
    @Size(max = 500, message = "Message must be less than 500 characters")
    private String notificationMessage;

    @Schema(description = "Type of the notification (user, recipe)", example = "user", allowableValues = {"user", "recipe"})
    @NotBlank(message = "Type is mandatory")
    private String notificationType;

    @Schema(description = "User ID of the recipient", example = "123e4567-e89b-12d3-a456-426614174001")
    @NotNull(message = "Recipient ID is mandatory")
    private UUID recipientId;
}
