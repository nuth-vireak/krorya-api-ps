package com.kshrd.krorya.controller;
import com.kshrd.krorya.model.dto.FollowDTO;
import com.kshrd.krorya.model.entity.AppUser;
import com.kshrd.krorya.model.entity.CustomUserDetail;
import com.kshrd.krorya.model.response.ApiResponse;
import com.kshrd.krorya.service.FollowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/follows")
@AllArgsConstructor
@Tag(name = "Follow Controller", description = "Endpoints for managing follows")
public class FollowController {

    private final FollowService followService;

    UUID getUsernameOfCurrentUser() {
        CustomUserDetail userDetails = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser appUser = userDetails.getAppUser();
        System.out.println("current user is : " + userDetails);
        return appUser.getUserId();
    }

    @SecurityRequirement(name="bearerAuth")
    @PostMapping("/follow/{followingId}")
    @Operation(summary = "Follow a user", description = "Follow a user")
    public ResponseEntity<ApiResponse<FollowDTO>> followUserByUserId(@PathVariable UUID followingId) {
        FollowDTO followDTO = followService.followerUserByUserId(followingId);
        ApiResponse<FollowDTO> apiResponse = ApiResponse.<FollowDTO>builder()
                .localDateTime(LocalDateTime.now())
                .message("Successfully followed user")
                .payload(followDTO)
                .code(201)
                .status(HttpStatus.CREATED)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @SecurityRequirement(name="bearerAuth")
    @DeleteMapping("/unfollow/{followingId}")
    @Operation(summary = "Unfollow a user", description = "Unfollow a user")
    public ResponseEntity<ApiResponse<FollowDTO>> unfollowUser(@PathVariable UUID followingId) {
        FollowDTO followDTO = followService.unfollowUserById(followingId);
        ApiResponse<FollowDTO> apiResponse = ApiResponse.<FollowDTO>builder()
                .localDateTime(LocalDateTime.now())
                .message("Successfully unfollowed user")
                .payload(followDTO)
                .code(200)
                .status(HttpStatus.OK)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/followers/{userId}")
    @Operation(summary = "Get followers of a user", description = "Get followers of a user")
    public ResponseEntity<ApiResponse<List<FollowDTO>>> getFollowersByUserId(@PathVariable UUID userId, Principal principal) {

        UUID currentUserId = null;
        if (principal != null){ //principal != null means that user has logged in
            currentUserId = getUsernameOfCurrentUser();
        }

        System.out.println("Current user id in profile controller : " + currentUserId);

        List<FollowDTO> followDTOList = followService.getFollowersByUserId(userId, currentUserId);
        ApiResponse<List<FollowDTO>> apiResponse = ApiResponse.<List<FollowDTO>>builder()
                .localDateTime(LocalDateTime.now())
                .message("Successfully fetched followers")
                .payload(followDTOList)
                .code(200)
                .status(HttpStatus.OK)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/followings/{userId}")
    @Operation(summary = "Get followings of a user", description = "Get followings of a user")
    public ResponseEntity<ApiResponse<List<FollowDTO>>> getFollowingsByUserId(@PathVariable UUID userId, Principal principal) {

        UUID currentUserId = null;
        if (principal != null){ //principal != null means that user has logged in
            currentUserId = getUsernameOfCurrentUser();
        }

        System.out.println("Current user id in profile controller : " + currentUserId);

        List<FollowDTO> followDTOList = followService.getFollowingsByUserId(userId, currentUserId);
        ApiResponse<List<FollowDTO>> apiResponse = ApiResponse.<List<FollowDTO>>builder()
                .localDateTime(LocalDateTime.now())
                .message("Successfully fetched followings")
                .payload(followDTOList)
                .code(200)
                .status(HttpStatus.OK)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

}