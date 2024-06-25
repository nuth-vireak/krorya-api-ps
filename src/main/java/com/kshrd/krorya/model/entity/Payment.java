package com.kshrd.krorya.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kshrd.krorya.model.enumeration.PaymentMethodEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Payment {
    private UUID transactionId;
    private UUID buyerId;
    private String paymentStatus;
    private PaymentMethodEnum paymentMethod;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
