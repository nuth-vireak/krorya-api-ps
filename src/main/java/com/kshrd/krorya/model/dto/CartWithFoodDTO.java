package com.kshrd.krorya.model.dto;


import com.kshrd.krorya.model.entity.AppUser;
import com.kshrd.krorya.model.entity.CartFood;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartWithFoodDTO {
    private UUID cartId;
    private SimpleAppUserDTO buyer;
    private SimpleAppUserDTO seller;
    private Boolean isOrder;
    private List<CartFood> foods;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
