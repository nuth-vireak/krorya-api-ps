package com.kshrd.krorya.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppUser {
    private UUID userId;
    private String username;
    private String profileImage;
    private String email;
    private String bio;
    private Integer followingsCount;
    private Integer followersCount;
    private String password;
    private Boolean isDeactivated;
    private String role;
}
