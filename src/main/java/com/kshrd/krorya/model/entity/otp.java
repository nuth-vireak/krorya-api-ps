package com.kshrd.krorya.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class otp {
    private UUID otpId;
    private String otpCode;
    private Date issuedAt;
    private Date expiresAt;
    private Boolean verify;
    private AppUser appUser;
    private UUID userId;
    private Boolean isVerifiedForget;
}
