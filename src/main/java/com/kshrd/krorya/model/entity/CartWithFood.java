package com.kshrd.krorya.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartWithFood {
    private UUID cartId;
    private AppUser buyer;
    private AppUser seller;
    private Boolean isOrder;
    private List<CartFood> foods;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
