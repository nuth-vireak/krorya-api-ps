package com.kshrd.krorya.model.request;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GroceryIngredientRequest {
    @NotNull(message = "isBought status cannot be null !! ")
    private boolean isBought;
}
