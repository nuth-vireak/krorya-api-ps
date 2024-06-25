package com.kshrd.krorya.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Address {
    private UUID addressId;
    private String location;
    private String buyerLatitude;
    private String buyerLongitude;

}
