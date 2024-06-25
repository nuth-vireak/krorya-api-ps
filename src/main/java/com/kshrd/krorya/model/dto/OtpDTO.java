package com.kshrd.krorya.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class OtpDTO {
    private static final Logger log = LoggerFactory.getLogger(OtpDTO.class);
    private String otpCode;
    private Date issuedDate;
    private Date expiresAt;
    private boolean verify;
    private AppUserDTO appUserDTO;
    private UUID userId;
    private boolean isVerifiedForget;

    public OtpDTO(String string, Date date, Date date1, boolean b, AppUserDTO appUserDTO, UUID userId) {
        this.otpCode = string;
        this.issuedDate = date;
        this.expiresAt = date1;
        this.verify = b;
        this.appUserDTO = appUserDTO;
        this.userId = userId;
    }


}