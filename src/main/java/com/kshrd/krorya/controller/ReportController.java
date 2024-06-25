package com.kshrd.krorya.controller;

import com.kshrd.krorya.model.dto.ReportDTO;
import com.kshrd.krorya.model.request.ReportOtherUserRequest;
import com.kshrd.krorya.model.request.ReportRecipeRequest;
import com.kshrd.krorya.model.response.ApiResponse;
import com.kshrd.krorya.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
@Tag(name = "Report Controller", description = "Endpoints for managing reports")
public class ReportController {
    private final ReportService reportService;

    @SecurityRequirement(name="bearerAuth")
    @Operation(summary = "Create a report of recipe")
    @PostMapping("/reports/recipes")
    public ResponseEntity<ApiResponse<ReportDTO>> createRecipeReport(@Valid @RequestBody ReportRecipeRequest reportRecipeRequest){
        ReportDTO report = reportService.createRecipeReport(reportRecipeRequest);
        return ResponseEntity.ok(
                ApiResponse.<ReportDTO>builder()
                        .message("A recipe report is created successfully")
                        .status(HttpStatus.CREATED)
                        .code(201)
                        .payload(report)
                        .build()
        );
    }

//////////////////////////////////////////////////////////////////////////////////////

    //create user report
    @SecurityRequirement(name="bearerAuth")
    @Operation(summary = "Create a report of other user")
    @PostMapping("/reports/user")
    public ResponseEntity<ApiResponse<ReportDTO>> createOtherUserReport(@Valid @RequestBody ReportOtherUserRequest reportOtherUserRequest){
        ReportDTO report = reportService.createOtherUserReport(reportOtherUserRequest);
        return ResponseEntity.ok(
                ApiResponse.<ReportDTO>builder()
                        .message("A report about other user is created successfully")
                        .status(HttpStatus.CREATED)
                        .code(201)
                        .payload(report)
                        .build()
        );
    }


}
