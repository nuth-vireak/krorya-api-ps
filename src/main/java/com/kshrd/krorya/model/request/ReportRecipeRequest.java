package com.kshrd.krorya.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportRecipeRequest {
    @NotNull(message = "Recipe id should not be null")
    private UUID recipeId;
    //no need reporterId in request class, it is current user id
    //private UUID reporterId;

    //private UUID reporteeId;
    @NotNull(message = "Report description should not be null")
    @NotBlank(message = "Report description should not be blank")
    private String description;
    //private Date reportDate;
    //private String reportType;
}
