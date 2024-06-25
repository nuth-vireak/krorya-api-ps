package com.kshrd.krorya.controller;

import com.kshrd.krorya.model.response.ApiResponse;
import com.kshrd.krorya.model.response.FileResponse;
import com.kshrd.krorya.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/files")
@Tag(name = "File Controller", description = "Controller for handling file operations with Amazon S3")
public class FileController {

    private final S3Service s3Service;

    public FileController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

//    @GetMapping
//    @Operation(summary = "Health check", description = "Endpoint to check the health status of the service")
//    public String health() {
//        return "UP";
//    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Upload file", description = "Endpoint to upload a file to Amazon S3")
    public ResponseEntity<ApiResponse<FileResponse>> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        String fileName = s3Service.uploadFile(file.getOriginalFilename(),file);
        String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath().path("api/v1/files/view/"+fileName).toUriString();
//        String fileUrl = s3Service.getFileUrl(fileName);
        FileResponse fileResponse = new FileResponse(fileName,file.getContentType(),file.getSize(),fileUrl);
        ApiResponse<FileResponse> apiResponse = ApiResponse.<FileResponse>builder()
                .message("File is uploaded successfully")
                .status(HttpStatus.CREATED)
                .code(201).localDateTime(LocalDateTime.now())
                .payload(fileResponse)
                .build();
        return  ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/download/{fileName}")
    @Operation(summary = "Download file", description = "Endpoint to download a file from Amazon S3")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String fileName) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(new InputStreamResource(s3Service.getFile(fileName).getObjectContent()));
    }

    @GetMapping("/view/{fileName}")
    @Operation(summary = "View file", description = "Endpoint to view an image file from Amazon S3")
    public ResponseEntity<InputStreamResource> viewFile(@PathVariable String fileName) {
        var s3Object = s3Service.getFile(fileName);
        System.out.println("This is my file name:" + s3Object);
        var content = s3Object.getObjectContent();
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .body(new InputStreamResource(content));
    }

    @DeleteMapping("/{fileName}")
    @Operation(summary = "Delete file", description = "Endpoint to delete a file from Amazon S3")
    public ResponseEntity<?> deleteFile(@PathVariable String fileName) {
        s3Service.deleteFile(fileName);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .message("File is deleted successfully")
                        .status(HttpStatus.OK)
                        .code(200)
                        .localDateTime(LocalDateTime.now())
                        .build()
        );
    }
}
