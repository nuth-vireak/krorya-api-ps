package com.kshrd.krorya.controller;

import com.kshrd.krorya.model.dto.AppUserDTO;
import com.kshrd.krorya.model.response.ApiResponse;
import com.kshrd.krorya.service.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import com.kshrd.krorya.exception.PasswordException;
import com.kshrd.krorya.model.entity.AppUser;
import com.kshrd.krorya.model.entity.CustomUserDetail;
import com.kshrd.krorya.model.request.ResetPasswordRequest;
import com.kshrd.krorya.model.request.UserRequest;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


import org.slf4j.Logger;

@RestController
@RequestMapping("api/v1/profiles")
@AllArgsConstructor
@Tag(name = "Profile controller", description = "Endpoints for managing users")
public class ProfileController {

    private static final Logger logger = LoggerFactory.getLogger(RecipeController.class);
    private final AppUserService appUserService;
    private final BCryptPasswordEncoder passwordEncoder;


    UUID getUsernameOfCurrentUser() {
        CustomUserDetail userDetails = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser appUser = userDetails.getAppUser();
        System.out.println("current user is : " + userDetails);

        return appUser.getUserId();
    }

    @Operation(summary = "get all user's profile ", description = "retrieve all user's profile ")
    @GetMapping
    public ResponseEntity<ApiResponse<List<AppUserDTO>>> getAllUsers(){

        List<AppUserDTO> allUsers = appUserService.getAllUsers();
        ApiResponse<List<AppUserDTO>> apiResponse = ApiResponse.<List<AppUserDTO>>builder()
                .localDateTime(LocalDateTime.now())
                .message("Get all users successfully")
                .payload(allUsers)
                .code(200)
                .status(HttpStatus.OK)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }


    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "get user by user's id ", description = "retrieve user by user id ")
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<AppUserDTO>> getUserByUserId(
            @Valid
            @Parameter(description = "User ID to retrieve user information",
                    example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
            @PathVariable UUID userId,
            Principal principal
    ){

        UUID currentUserId = null;
        if (principal != null){ //principal != null means that user has logged in
            currentUserId = getCurrentUserId();
        }

//        UUID currentUserId = null;
//        if (principal != null){ //principal != null means that user has logged in
//            logger.info("Principal is not null");
//            currentUserId = getCurrentUserId();
//            if (currentUserId == null) {
//                logger.info("Current user ID is null after calling getCurrentUserId");
//            }
//        } else {
//            logger.info("Principal is null");
//        }

//        currentUserId = getCurrentUserId();
//        System.out.println("Current user id in profile controller : " + currentUserId);
        AppUserDTO appUserDTO = appUserService.findUserByUserId(currentUserId, userId);

        ApiResponse<AppUserDTO> apiResponse = ApiResponse.<AppUserDTO>builder()
                .status(HttpStatus.OK)
                .localDateTime(LocalDateTime.now())
                .message("Get user id = " + userId + " successfully")
                .payload(appUserDTO)
                .code(200)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "get current user information", description = "retrieve current user detail")
    @GetMapping("/current-user")
    public ResponseEntity<ApiResponse<AppUserDTO>> getCurrentUserInfo(){
        AppUserDTO appUserDTO = appUserService.getCurrentUserInfo();
        UUID userId = appUserDTO.getUserId();
        ApiResponse<AppUserDTO> apiResponse = ApiResponse.<AppUserDTO>builder()
                .status(HttpStatus.OK)
                .localDateTime(LocalDateTime.now())
                .message("Current user of ID " + userId + " is retrieved successfully")
                .payload(appUserDTO)
                .code(200)
                .build();
        return ResponseEntity.ok(apiResponse);

    }

    @SecurityRequirement(name="bearerAuth")
    @Operation(summary = "Update user's profile", description = "update profile of current user")
    @PutMapping
    public ResponseEntity<ApiResponse<AppUserDTO>> updateUser(@Valid @RequestBody UserRequest userRequest){
        UUID userId = getUsernameOfCurrentUser();
        AppUserDTO appUserDTO = appUserService.updateUserById(userId, userRequest);
        return ResponseEntity.ok(
                ApiResponse.<AppUserDTO>builder()
                        .message("Your profile information is updated successfully!")
                        .status(HttpStatus.OK)
                        .code(200)
                        .payload(appUserDTO)
                        .localDateTime(LocalDateTime.now())
                        .build()
        );
    }

    @SecurityRequirement(name="bearerAuth")
    @Operation(summary = "Reset user's password", description = "Set new password for user")
    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest){
        UUID userId = getUsernameOfCurrentUser();
        if (!resetPasswordRequest.getNewPassword().equals(resetPasswordRequest.getConfirmPassword())) {
            throw new PasswordException("Password is not matched");
        }
        appUserService.changeUserPassword(resetPasswordRequest, userId);
        return ResponseEntity.ok(
                ApiResponse.<AppUserDTO>builder()
                        .message("Your password is updated successfully!")
                        .status(HttpStatus.OK)
                        .code(200)
                        .localDateTime(LocalDateTime.now())
                        .build()
        );
    }


    private UUID getCurrentUserId() {
        CustomUserDetail userDetails = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getAppUser().getUserId();
    }

}
