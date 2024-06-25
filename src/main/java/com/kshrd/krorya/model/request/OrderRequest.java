package com.kshrd.krorya.model.request;

import com.kshrd.krorya.model.enumeration.IsOrderRequestEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
//    private String description;
//    private BigDecimal totalAmount;
//    private Date createdAt;
//    private Date updatedAt;

    private UUID cartId;
    private UUID addressId;
    private String description;
    private BigDecimal totalAmount;
//    private String status;
    private String phoneNumber;
//    private IsOrderRequestEnum isOrderRequest;
//    private Date createdAt;
//    private Date updatedAt;
}
