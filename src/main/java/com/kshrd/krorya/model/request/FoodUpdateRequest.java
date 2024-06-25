package com.kshrd.krorya.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodUpdateRequest {

    @Schema(description = "Id of the food to update", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
    @NotNull(message = "Food ID cannot be null")
    private UUID foodId;

    @Schema(description = "Bookmarked status of the food", example = "true")
    @NotNull(message = "Bookmarked status cannot be null")
    private boolean bookmarked;
}
