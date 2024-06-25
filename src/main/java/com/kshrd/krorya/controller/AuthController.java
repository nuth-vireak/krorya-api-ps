package com.kshrd.krorya.controller;

import com.kshrd.krorya.convert.AppUserConvertor;
import com.kshrd.krorya.convert.OtpDTOConvertor;
import com.kshrd.krorya.exception.CustomNotFoundException;
import com.kshrd.krorya.exception.CustomUnauthorizedException;
import com.kshrd.krorya.exception.PasswordException;
import com.kshrd.krorya.exception.SearchNotFoundException;
import com.kshrd.krorya.model.dto.AppUserDTO;
import com.kshrd.krorya.model.dto.OtpDTO;
import com.kshrd.krorya.model.entity.CustomUserDetail;
import com.kshrd.krorya.model.request.AppUserRequest;
import com.kshrd.krorya.model.request.AuthRequest;
import com.kshrd.krorya.model.request.GoogleUserRequest;
import com.kshrd.krorya.model.request.PasswordRequest;
import com.kshrd.krorya.model.response.ApiResponse;
import com.kshrd.krorya.model.response.AuthResponse;
import com.kshrd.krorya.security.JwtService;
import com.kshrd.krorya.service.AppUserService;
import com.kshrd.krorya.service.EmailService;
import com.kshrd.krorya.service.OtpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

@RestController
@RequestMapping("api/v1/auth")
@AllArgsConstructor
@Slf4j
@Tag(name = "Auth Controller", description = "Endpoints for managing Authentication")
public class AuthController {
    private final AppUserService appUserService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final OtpService otpService;
    private final EmailService emailService;
    private final OtpDTOConvertor otpDTOConvertor;
    private final AppUserConvertor appUserConvertor;

    private void authenticate(String username, String password) throws Exception {
        log.info("Attempting to authenticate user: {}", username);
        try {
            UserDetails userApp = appUserService.loadUserByUsername(username);
            log.info("User found: {}", userApp.getUsername());

            if (!passwordEncoder.matches(password, userApp.getPassword())) {
                log.warn("Password mismatch for user: {}", username);
                throw new CustomNotFoundException("Wrong Password");
            }

            log.info("Password matched for user: {}", username);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
//            System.out.println("after authentication");
            log.info("the usernamePasswordAuthenticationToken : {}", authenticationToken);
//            System.out.println(authenticationManager.authenticate(authenticationToken));
            authenticationManager.authenticate(authenticationToken);
            log.info("Authentication successful for user: {}", username);

        } catch (DisabledException e) {
            log.error("User disabled: {}", username, e);
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            log.error("Invalid credentials for user: {}", username, e);
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @Operation(summary = "Refresh Token", description = "Generate a new access token using the refresh token")
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody String refreshToken) {
        String username = jwtService.extractUsername(refreshToken);
        UserDetails userDetails = appUserService.loadUserByUsername(username);

        if (jwtService.validateToken(refreshToken, userDetails)) {
            String newAccessToken = jwtService.generateAccessToken(userDetails);
            AuthResponse authResponse = new AuthResponse(newAccessToken, refreshToken);
            return ResponseEntity.ok(authResponse);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid refresh token");
        }
    }

    @Operation(summary = "Forget Password", description = "Reset the user password")
    @PutMapping("/forget")
    public ResponseEntity<?> forget(@Valid @RequestBody PasswordRequest passwordRequest, @RequestParam String email) {
        if (!(Objects.equals(passwordRequest.getPassword(), passwordRequest.getConfirmPassword()))) {
            throw new PasswordException("Password is not matched");
        }
        AppUserDTO userDTO = appUserConvertor.toDTO(appUserService.findUserByEmail(email));
        if (userDTO == null) {
            throw new SearchNotFoundException("Email: " + email + " not found");
        }

        otpService.resetPasswordByEmail(passwordRequest, email);

        ApiResponse<String> apiResponse = ApiResponse.<String>builder()
                .payload("Password Reset")
                .message("Password Reset successfully")
                .code(201)
                .status(HttpStatus.OK)
                .localDateTime(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/google-register")
    public ResponseEntity<?> registerGoogleUser(@RequestBody GoogleUserRequest googleUserRequest) {
            // If user doesn't exist, create a new user from Google
            CustomUserDetail customUserDetail = appUserService.createUserFromGoogle(googleUserRequest);
            // No OTP generation and sending for Google registration
            final String accessToken = jwtService.generateAccessToken(customUserDetail);
            final String refreshToken = jwtService.generateRefreshToken(customUserDetail);
            AuthResponse authResponse = new AuthResponse(accessToken, refreshToken);
            return ResponseEntity.ok(authResponse);

    }

    @Operation(summary = "User Registration", description = "Register a new user and send OTP for verification")
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody AppUserRequest appUserRequest) throws MessagingException, IOException {

        AppUserDTO appUserDTO = appUserService.createUser(appUserRequest);

        ApiResponse<AppUserDTO> response = ApiResponse.<AppUserDTO>builder()
                .message("Successfully Registered")
                .status(HttpStatus.CREATED)
                .code(201)
                .payload(appUserDTO)
                .localDateTime(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Verify OTP", description = "Verify the OTP code sent to the user")
    @PutMapping("/verify")
    public ResponseEntity<?> verify(
            @Valid
            @Parameter(description = "OTP code sent to the user", example = "123456")
            @RequestParam @NotBlank @NotNull String otpCode) {

        OtpDTO otpDTO = otpService.findByCode(otpCode);
        otpService.verify(otpCode);
        log.info("OTP found: {}", otpDTO);

        ApiResponse<String> apiResponse = ApiResponse.<String>builder()
                .status(HttpStatus.OK)
                .code(201)
                .localDateTime(LocalDateTime.now())
                .message("Your OTP has been verified")
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @Operation(summary = "Verify OTP", description = "Verify the OTP code sent to the user")
    @PutMapping("/verify-forget-password")
    public ResponseEntity<?> verifyForgetPassword(
            @Valid
            @Parameter(description = "OTP code sent to the user", example = "123456")
            @RequestParam @NotBlank @NotNull String otpCode) {

        OtpDTO otpDTO = otpService.findByCode(otpCode);
        otpService.verifyForgetPassword(otpCode);


        ApiResponse<String> apiResponse = ApiResponse.<String>builder()
                .status(HttpStatus.OK)
                .code(201)
                .localDateTime(LocalDateTime.now())
                .message("Your OTP has been verified")
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @Operation(summary = "Resend OTP", description = "Resend OTP to the user's email")
    @PostMapping("/resend")
    public ResponseEntity<?> resend(@Valid
                                    @Parameter(description = "Email of the user to resend OTP", example = "example@gmail.com")
                                    @RequestParam @Email String email) throws MessagingException, IOException {

        otpService.regenerateAndResendOTP(email);
        ApiResponse<String> apiResponse = ApiResponse.<String>builder()
                .status(HttpStatus.OK)
                .code(201)
                .localDateTime(LocalDateTime.now())
                .message("The OTP has been sent to the email. Please check your mail")
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @Operation(summary = "User Login", description = "Authenticate user and return JWT tokens")
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest authRequest) throws Exception {
        AppUserDTO appUserDTO = appUserConvertor.toDTO(appUserService.findUserByEmail(authRequest.getEmail()));
        if (appUserDTO == null) {
            throw new SearchNotFoundException("Email : " + authRequest.getEmail() + " not found");
        }
        OtpDTO otpDTO = otpDTOConvertor.toDTO(otpService.findOtpByUserId(appUserDTO.getUserId()));
        if (otpDTO == null || !otpDTO.isVerify()) {
            throw new CustomUnauthorizedException("Your email is not verified");
        }
        authenticate(authRequest.getEmail(), authRequest.getPassword());
        final UserDetails userDetails = appUserService.loadUserByUsername(authRequest.getEmail());
        final String accessToken = jwtService.generateAccessToken(userDetails);
        final String refreshToken = jwtService.generateRefreshToken(userDetails);
        AuthResponse authResponse = new AuthResponse(accessToken, refreshToken);
        return ResponseEntity.ok(authResponse);
    }

}