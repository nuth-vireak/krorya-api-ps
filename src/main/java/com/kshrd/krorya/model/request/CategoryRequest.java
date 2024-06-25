package com.kshrd.krorya.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {
    @NotNull(message = "Category name should not be null")
    @NotBlank(message = "Category name should not be blank")
    private String categoryName;

    @NotNull(message = "Category name should not be null")
    private String categoryIcon;
}
