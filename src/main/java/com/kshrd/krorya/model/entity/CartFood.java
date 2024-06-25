package com.kshrd.krorya.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartFood {
    private UUID cartFoodId;
    private UUID cartId;
    private Food food;
    private Integer qty;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

