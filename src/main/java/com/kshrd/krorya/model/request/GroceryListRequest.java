package com.kshrd.krorya.model.request;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class GroceryListRequest {
    @NotBlank(message = "Title is required, cannot be blank")
    private String title;
}
