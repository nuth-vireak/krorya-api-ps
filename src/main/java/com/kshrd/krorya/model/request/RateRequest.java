package com.kshrd.krorya.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RateRequest {
    @NotNull(message = "Food id should not be null")
    private UUID foodId;

    @NotNull(message = "Rated star should not be null")
    @Positive(message = "Rate star must be positive")
    private Integer rateStar;
}
