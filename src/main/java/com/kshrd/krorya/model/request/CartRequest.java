package com.kshrd.krorya.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartRequest {
    private UUID foodId;
    private Integer quantity;
    private Date createdAt;
    private Date updatedAt;
}
