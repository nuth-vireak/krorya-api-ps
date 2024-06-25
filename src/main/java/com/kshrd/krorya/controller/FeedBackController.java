package com.kshrd.krorya.controller;

import com.kshrd.krorya.model.dto.FeedbackDTO;
import com.kshrd.krorya.model.request.FeedbackRequest;
import com.kshrd.krorya.model.response.ApiResponse;
import com.kshrd.krorya.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/feedbacks")
@AllArgsConstructor
@Tag(name = "Feedback Controller", description = "Endpoints for managing feedbacks")
public class FeedBackController {

    private final FeedbackService feedbackService;

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    @Operation(summary = "Insert feedback", description = "Insert feedback")
    public ResponseEntity<ApiResponse<FeedbackDTO>> insertFeedback(@Valid @RequestBody FeedbackRequest feedbackRequest) {
        FeedbackDTO feedbackDTO = feedbackService.insertFeedback(feedbackRequest);
        ApiResponse<FeedbackDTO> apiResponse = ApiResponse.<FeedbackDTO>builder()
                .message("Feedback inserted successfully")
                .status(HttpStatus.CREATED)
                .code(201)
                .payload(feedbackDTO)
                .localDateTime(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/reply")
    @Operation(summary = "Reply to feedback", description = "Reply to feedback")
    public ResponseEntity<ApiResponse<FeedbackDTO>> replyToFeedback(@Valid @RequestBody FeedbackRequest feedbackRequest) {
        FeedbackDTO feedbackDTO = feedbackService.replyToFeedback(feedbackRequest);
        ApiResponse<FeedbackDTO> apiResponse = ApiResponse.<FeedbackDTO>builder()
                .message("Feedback replied successfully")
                .status(HttpStatus.CREATED)
                .code(201)
                .payload(feedbackDTO)
                .localDateTime(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("/food/{foodId}")
    @Operation(summary = "Get feedbacks by food id", description = "Get feedbacks by food id")
    public ResponseEntity<ApiResponse<List<FeedbackDTO>>> getFeedbacksByFoodId(@Valid @NotNull @PathVariable("foodId") UUID foodId) {
        List<FeedbackDTO> feedbackDTOList = feedbackService.getFeedbacksByFoodId(foodId);
        ApiResponse<List<FeedbackDTO>> apiResponse = ApiResponse.<List<FeedbackDTO>>builder()
                .message("Feedbacks retrieved successfully")
                .status(HttpStatus.OK)
                .code(200)
                .payload(feedbackDTOList)
                .localDateTime(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
