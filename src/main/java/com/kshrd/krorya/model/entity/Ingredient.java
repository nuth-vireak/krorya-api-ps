package com.kshrd.krorya.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ingredient {
    private UUID ingredientId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UUID userId;
    private String ingredientName;
    private String ingredientIcon;
    private String ingredientType;
    private String quantity;

}

