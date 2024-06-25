package com.kshrd.krorya.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
    private UUID cartId;
    private SimpleAppUserDTO buyer;
    private SimpleAppUserDTO seller;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
