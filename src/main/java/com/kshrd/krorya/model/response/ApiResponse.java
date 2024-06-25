package com.kshrd.krorya.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    @Schema(description = "Message describing the API response", example = "Success")
    private String message;

    @Schema(description = "HTTP status of the API response", example = "200")
    private HttpStatus status;

    @Schema(description = "Error code associated with the API response", example = "404")
    private Integer code;

    @Schema(description = "Payload data of the API response")
    private T payload;

    @Schema(description = "Timestamp when the API response was generated", example = "2024-06-18T10:15:30")
    private LocalDateTime localDateTime;

    public ApiResponse(String message, int value, LocalDateTime now, HttpStatus httpStatus) {
        this.message = message;
        this.status = httpStatus;
        this.code = value;
        this.localDateTime = now;
    }
}
