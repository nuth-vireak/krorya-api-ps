package com.kshrd.krorya.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    @NotEmpty(message = "Email should not be empty")
    private String email;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "Password must be at least 8 characters long, with at least one letter and one number")
    @NotBlank(message = "Password is required")
    @NotEmpty(message = "Password should not be empty")
    private String password;

}