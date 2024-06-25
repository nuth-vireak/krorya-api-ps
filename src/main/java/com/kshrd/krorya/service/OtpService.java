package com.kshrd.krorya.service;

import com.kshrd.krorya.model.dto.OtpDTO;
import com.kshrd.krorya.model.entity.AppUser;
import com.kshrd.krorya.model.entity.otp;
import com.kshrd.krorya.model.request.PasswordRequest;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
public interface OtpService {
    OtpDTO findById(Integer id);

    void save(OtpDTO OtpDTO);

    OtpDTO findByCode(String code);

    void uploadOtp(String otpCode);

    void verify(String otpCode);

    void verifyForgetPassword(String otpCode);

    void regenerateAndResendOTP(String email) throws MessagingException, IOException;

    void resetPasswordByEmail(PasswordRequest passwordRequest, String email);

    otp findOtpByUserId(UUID userId);

    OtpDTO generateOtp(Integer length, AppUser appUser);
}
