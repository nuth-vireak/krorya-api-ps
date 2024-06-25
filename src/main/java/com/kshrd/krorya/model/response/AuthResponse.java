package com.kshrd.krorya.model.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Data
public class AuthResponse {
    private String accessToken;
    private String refreshToken;

    public AuthResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
