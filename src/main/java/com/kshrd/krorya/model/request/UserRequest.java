package com.kshrd.krorya.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    @NotBlank(message = "User name should not be blank")
    @NotNull(message = "User name should not be null")
    private String username;

    @NotBlank(message = "Profile image should be required")
    private String profileImage;

    @NotBlank(message = "User bio should not be empty")
    private String bio;
}
