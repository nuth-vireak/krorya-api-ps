package com.kshrd.krorya.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportOtherUserRequest {
    @NotNull(message = "Reportee id should not be null")
    private UUID reporteeId;

    @NotNull(message = "Report description should not be null")
    @NotBlank(message = "Report description should not be blank")
    private String description;
}
