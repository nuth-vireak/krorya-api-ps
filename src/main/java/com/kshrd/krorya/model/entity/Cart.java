package com.kshrd.krorya.model.entity;

import com.kshrd.krorya.model.dto.AppUserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    private UUID cartId;
    private AppUser buyer;
    private AppUser seller;
    private Boolean isOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
