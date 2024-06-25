package com.kshrd.krorya.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rate {
    private UUID rateId;
    private String foodName;
    private String raterName;
    private Integer rateStar;
}
