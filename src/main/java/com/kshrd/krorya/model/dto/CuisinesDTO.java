package com.kshrd.krorya.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CuisinesDTO {
    private UUID cuisineId;
    private String cuisineName;
}
