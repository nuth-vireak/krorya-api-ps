package com.kshrd.krorya.model.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodRequest {
    @NotBlank(message = "Food name cannot be blank")
    @NotNull(message = "Food name should not be null")
    private String foodName;

    @NotBlank(message = "Food description cannot be blank")
    @NotNull(message = "Food description should not be null")
    private String foodDescription;

    @NotBlank(message = "Food image cannot be blank")
    @NotNull(message = "Food image should not be null")
    private String foodImage;

    @Positive
    @NotNull(message = "Food price should not be null")
    private BigDecimal foodPrice;

    @NotNull(message = "Food category should not be null")
    private UUID categoryId;
}
