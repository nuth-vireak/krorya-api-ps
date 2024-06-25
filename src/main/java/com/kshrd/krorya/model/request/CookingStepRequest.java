package com.kshrd.krorya.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CookingStepRequest {
    private Integer stepNumber;
    private String image;
    private String description;
}
