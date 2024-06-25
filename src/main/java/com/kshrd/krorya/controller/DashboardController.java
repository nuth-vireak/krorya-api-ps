package com.kshrd.krorya.controller;

import com.kshrd.krorya.model.dto.ReportDTO;
import com.kshrd.krorya.model.response.ApiResponse;
import com.kshrd.krorya.repository.ReportRepository;
import com.kshrd.krorya.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
@Secured("ROLE_ADMIN")
@Tag(name = "Dashboard Controller", description = "Endpoints for managing dashboard")

public class DashboardController {
    private final ReportService reportService;
    private final ReportRepository reportRepository;

    @SecurityRequirement(name="bearerAuth")
    @Operation(summary = "Get number of food")
    @GetMapping("/admin/dashboard/number-of-food")
    public ResponseEntity<ApiResponse<Integer>> getNumberOfFood(){
        Integer allFoods = reportRepository.countAllFoods();
        return ResponseEntity.ok(
                ApiResponse.<Integer>builder()
                        .message("Number of food is fetched successfully")
                        .status(HttpStatus.OK)
                        .code(200)
                        .payload(allFoods)
                        .build()
        );
    }

    @SecurityRequirement(name="bearerAuth")
    @Operation(summary = "Get number of user")
    @GetMapping("/admin/dashboard/number-of-user")
    public ResponseEntity<ApiResponse<Integer>> getNumberOfUser(){
        Integer allUsers = reportRepository.countAllUsers();
        return ResponseEntity.ok(
                ApiResponse.<Integer>builder()
                        .message("Number of user is fetched successfully")
                        .status(HttpStatus.OK)
                        .code(200)
                        .payload(allUsers)
                        .build()
        );
    }
    @SecurityRequirement(name="bearerAuth")
    @Operation(summary = "Get number of recipe")
    @GetMapping("/admin/dashboard/number-of-recipe")
    public ResponseEntity<ApiResponse<Integer>> getNumberOfRecipe(){
        Integer allRecipes = reportRepository.countAllRecipes();
        return ResponseEntity.ok(
                ApiResponse.<Integer>builder()
                        .message("Number of recipe is fetched successfully")
                        .status(HttpStatus.OK)
                        .code(200)
                        .payload(allRecipes)
                        .build()
        );
    }

    @SecurityRequirement(name="bearerAuth")
    @Operation(summary = "Get number of reported user")
    @GetMapping("/admin/dashboard/number-of-reported-user")
    public ResponseEntity<ApiResponse<Integer>> getNumberOfReportedUser(){
        Integer allReportedUser = reportRepository.countReportedUser();
        return ResponseEntity.ok(
                ApiResponse.<Integer>builder()
                        .message("Number of reported user is fetched successfully")
                        .status(HttpStatus.OK)
                        .code(200)
                        .payload(allReportedUser)
                        .build()
        );
    }

    @SecurityRequirement(name="bearerAuth")
    @Operation(summary = "Get number of reported recipe")
    @GetMapping("/admin/dashboard/number-of-reported-recipe")
    public ResponseEntity<?> getNumberOfReportedRecipe(){
        Integer allReportedRecipe = reportRepository.countReportedRecipe();
        return ResponseEntity.ok(
                ApiResponse.<Integer>builder()
                        .message("Number of reported recipe is fetched successfully")
                        .status(HttpStatus.OK)
                        .code(200)
                        .payload(allReportedRecipe)
                        .build()
        );
    }

    @SecurityRequirement(name="bearerAuth")
    @Operation(summary = "Get all reported recipes")
    @GetMapping("/admin/dashboard/reported-recipes")
    public ResponseEntity<ApiResponse<List<ReportDTO>>> getAllReportedRecipes(){
        List<ReportDTO> reportList = reportService.getAllReportedRecipes();
        return ResponseEntity.ok(
                ApiResponse.<List<ReportDTO>>builder()
                        .message("All reported recipes are fetched successfully")
                        .status(HttpStatus.OK)
                        .code(200)
                        .payload(reportList)
                        .build()
        );
    }

    @SecurityRequirement(name="bearerAuth")
    @Operation(summary = "Get a report of recipe by reported recipe id")
    @GetMapping("/admin/dashboard/reported-recipes/{reportedRecipeId}")
    public ResponseEntity<ApiResponse<ReportDTO>> getReportedRecipeByRecipeReportId(
            @Parameter(description = "Report ID to retrieve a reported recipe", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
            @PathVariable UUID reportedRecipeId){
        ReportDTO report = reportService.getReportedRecipeByRecipeReportId(reportedRecipeId);
        return ResponseEntity.ok(
                ApiResponse.<ReportDTO>builder()
                        .message("A report of recipe is fetched successfully")
                        .status(HttpStatus.OK)
                        .code(200)
                        .payload(report)
                        .build()
        );
    }

    @SecurityRequirement(name="bearerAuth")
    @Operation(summary = "Delete a reported recipe and recipe report")
    @DeleteMapping("/admin/dashboard/reported-recipes/{reportedRecipeId}")
    public ResponseEntity<ApiResponse<ReportDTO>> deleteReportedRecipeByReportId(
            @Parameter(description = "Report ID to delete a reported recipe and recipe report", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
            @PathVariable UUID reportedRecipeId){
        reportService.deleteReportedRecipeByReportId(reportedRecipeId);
        return ResponseEntity.ok(
                ApiResponse.<ReportDTO>builder()
                        .message("A reported recipe of report ID " + reportedRecipeId + " is deleted successfully")
                        .status(HttpStatus.OK)
                        .code(200)
                        .build()
        );
    }


    @SecurityRequirement(name="bearerAuth")
    @Operation(summary = "Get all reported users")
    @GetMapping("/admin/dashboard/reported-users")
    public ResponseEntity<ApiResponse<List<ReportDTO>>> getAllReportedUsers(){
        List<ReportDTO> reportList = reportService.getAllReportedUser();
        return ResponseEntity.ok(
                ApiResponse.<List<ReportDTO>>builder()
                        .message("All reported users are fetched successfully")
                        .status(HttpStatus.OK)
                        .code(200)
                        .payload(reportList)
                        .build()
        );
    }

    @SecurityRequirement(name="bearerAuth")
    @Operation(summary = "Get a report of user by reported user id")
    @GetMapping("/admin/dashboard/reported-users/{reportedUserId}")
    public ResponseEntity<ApiResponse<ReportDTO>> getReportedUserByUserReportId(
            @Parameter(description = "Report ID to retrieve a reported recipe", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
            @PathVariable UUID reportedUserId){
        ReportDTO report = reportService.getReportedUserByUserReportId(reportedUserId);
        return ResponseEntity.ok(
                ApiResponse.<ReportDTO>builder()
                        .message("A report of user is fetched successfully")
                        .status(HttpStatus.OK)
                        .code(200)
                        .payload(report)
                        .build()
        );
    }

    @SecurityRequirement(name="bearerAuth")
    @Operation(summary = "Ban a reported user and delete user report")
    @DeleteMapping("/admin/dashboard/reported-users/{reportedUserId}")
    public ResponseEntity<ApiResponse<ReportDTO>> banReportedUserAndDeleteReportedUserByUserReportId(
            @Parameter(description = "User report ID to ban reported user and delete user report", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
            @PathVariable UUID reportedUserId){
        reportService.banReportedUserAndDeleteReportedUserByUserReportId(reportedUserId);
        return ResponseEntity.ok(
                ApiResponse.<ReportDTO>builder()
                        .message("A reported user of report ID " + reportedUserId + " is banned successfully")
                        .status(HttpStatus.OK)
                        .code(200)
                        .build()
        );
    }

}
