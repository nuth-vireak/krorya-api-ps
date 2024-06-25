package com.kshrd.krorya.model.entity;

import com.kshrd.krorya.model.enumeration.IsOrderRequestEnum;
import com.kshrd.krorya.model.enumeration.OrderStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private UUID orderId;
    private Cart cartId;
    private UUID paymentId;
    private UUID addressId;
    private String description;
    private BigDecimal totalAmount;
    private OrderStatusEnum status;
    private String phoneNumber;
    private IsOrderRequestEnum isOrderRequest;
    private Date createdAt;
    private Date updatedAt;
}
