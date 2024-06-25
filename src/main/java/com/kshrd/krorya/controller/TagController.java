package com.kshrd.krorya.controller;

import com.kshrd.krorya.model.dto.TagDTO;
import com.kshrd.krorya.model.response.ApiResponse;
import com.kshrd.krorya.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/v1/tags")
@AllArgsConstructor
@Tag(name = "Tag Controller", description = "Endpoints for managing Tags")
public class TagController {

    private final TagService tagService;
    private static final Logger logger = LoggerFactory.getLogger(TagController.class);

    @GetMapping
    @Operation(summary = "Get all tags", description = "Retrieve a list of all tags available in the system")
    public ResponseEntity<ApiResponse<List<TagDTO>>> getTags() {
        logger.info("Fetching all tags");

        List<TagDTO> tags;
        try {
            tags = tagService.getTags();
            String message = tags.isEmpty() ? "No tags found" : "Tags retrieved successfully";
            ApiResponse<List<TagDTO>> apiResponse = ApiResponse.<List<TagDTO>>builder()
                    .status(HttpStatus.OK)
                    .localDateTime(LocalDateTime.now())
                    .code(HttpStatus.OK.value())
                    .payload(tags)
                    .message(message)
                    .build();

            logger.info("Tags retrieved: {}", tags.size());
            return ResponseEntity.ok(apiResponse);

        } catch (Exception e) {
            logger.error("Error retrieving tags", e);
            ApiResponse<List<TagDTO>> apiResponse = ApiResponse.<List<TagDTO>>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .localDateTime(LocalDateTime.now())
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Failed to retrieve tags")
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        }
    }
}
