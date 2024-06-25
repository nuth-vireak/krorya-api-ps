package com.kshrd.krorya.service.serviceImplementation;

import com.amazonaws.services.kms.model.ConflictException;
import com.kshrd.krorya.convert.AppUserConvertor;
import com.kshrd.krorya.convert.OtpDTOConvertor;
import com.kshrd.krorya.exception.CustomBadRequestException;
import com.kshrd.krorya.exception.CustomNotFoundException;
import com.kshrd.krorya.exception.DuplicatedException;
import com.kshrd.krorya.model.dto.AppUserDTO;
import com.kshrd.krorya.model.dto.OtpDTO;
import com.kshrd.krorya.model.entity.AppUser;
import com.kshrd.krorya.model.entity.otp;
import com.kshrd.krorya.model.request.PasswordRequest;
import com.kshrd.krorya.repository.OtpRepository;
import com.kshrd.krorya.service.OtpService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {
    private static final Logger log = LoggerFactory.getLogger(OtpServiceImpl.class);
    private final OtpRepository otpRepository;
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final OtpDTOConvertor otpDTOConvertor;
    private final AppUserConvertor appUserConvertor;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public OtpDTO findById(Integer id) {
        return null;
    }

    @Override
    public void save(OtpDTO otpDTO) {
        log.info("OtpDTO in OtpServiceImpl: {}",otpDTO);
        otpRepository.createNewOtp(otpDTO);
    }

    @Override
    public OtpDTO findByCode(String code) {

        if (!code.matches("\\d+")) {
            throw new CustomBadRequestException("Invalid OTP code. OTP must contain only digits.");
        }
        if (code.length() != 6) {
            throw new CustomBadRequestException("Invalid OTP code. OTP must be exactly 6 digits long.");
        }

        OtpDTO otpDTO = otpRepository.findByCode(code);

        if (otpDTO == null) {
            throw new CustomNotFoundException("Invalid OTP code.");
        }

//        verify(code);

        return otpDTO;
    }

    @Override
    public void uploadOtp(String otpCode) {
        // Implement as needed
    }

    @Override
    public void verify(String otpCode) {
        OtpDTO otpDTO = otpRepository.findByCode(otpCode);
        if (otpDTO.isVerify()){
            throw new CustomBadRequestException("This otp already verified!");
        }
        otpRepository.verify(otpCode);
    }

    @Override
    public void verifyForgetPassword(String otpCode) {
        OtpDTO otpDTO = otpRepository.findByCode(otpCode);
        if (otpDTO.isVerifiedForget()){
            throw new CustomBadRequestException("This otp already verified!");
        }
        otpRepository.verifyForgetPassword(otpCode);
    }

    @Override
    public void regenerateAndResendOTP(String email) throws MessagingException, IOException {
        otp existingOtp = otpRepository.findByEmail(email);
        if (existingOtp == null) {
            throw new CustomNotFoundException("The email does not exist");
        }
//        if (existingOtp.getVerify()) {
//            throw new IllegalStateException("Email is already verified");
//        }
        log.info("Existing Otp: {}", existingOtp);
        Integer otpLength = 6;
        OtpDTO newOtp = generateOtp(otpLength, existingOtp.getAppUser());
        existingOtp.setOtpCode(newOtp.getOtpCode());
        existingOtp.setExpiresAt(newOtp.getExpiresAt());
        otpRepository.update(otpDTOConvertor.toDTO(existingOtp));
        sendOtpEmail(email, newOtp.getOtpCode());
        log.info("New Otp: {}", newOtp);
    }

    private void sendOtpEmail(String email, String otpCode) throws MessagingException {
        log.info("Sending OTP email to: {}", email);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(fromEmail);
        helper.setTo(email);
        helper.setSubject("Your OTP for Email Verification");

        Context context = new Context();
        context.setVariable("otp", otpCode);
        String htmlContent = templateEngine.process("otp-mail", context);
        helper.setText(htmlContent, true);

        mailSender.send(message);
        log.info("OTP email sent to: {}", email);
    }

    @Override
    public void resetPasswordByEmail(PasswordRequest passwordRequest, String email) {
        System.out.println(passwordRequest.getPassword());
        otp existingOtp = otpRepository.findByEmail(email);
        System.out.println(existingOtp);
        System.out.println("This is the result" + existingOtp.getIsVerifiedForget());
        if (!existingOtp.getIsVerifiedForget()) {
            throw new CustomBadRequestException("OTP didn't verify");
        }
        String newPassword = passwordEncoder.encode(passwordRequest.getPassword());
        otpRepository.changePassword(newPassword, email);
        otpRepository.verifyForgetPasswordToFalse(existingOtp.getOtpCode());
    }

    @Override
    public otp findOtpByUserId(UUID userId) {
        log.info("Fetching OTP for user: {}", userId);
        otp otp = otpRepository.findOtpByUserId(userId);
        System.out.println(otp + " after finding the user by id" );
        log.info("Retrieved OTP: {}", otp);
        if (otp != null) {
            log.info("AppUserDTO in OTP: {}", otp.getAppUser());
        }
        return otp;
    }

    @Override
    public OtpDTO generateOtp(Integer length, AppUser appUser) {
        log.info("AppUser : {}",appUser);
        final long expiryInterval = 5L * 60 * 1000; // 5 minutes
        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < length; i++) {
            otp.append(random.nextInt(10));
        }
        return new OtpDTO(otp.toString(), new Date(), new Date(System.currentTimeMillis() + expiryInterval), false, appUserConvertor.toDTO(appUser), appUser.getUserId(), false);
    }
}
