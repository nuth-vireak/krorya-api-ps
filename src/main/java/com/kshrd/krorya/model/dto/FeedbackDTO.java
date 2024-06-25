package com.kshrd.krorya.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FeedbackDTO {
    private UUID feedbackId;
    private UUID parentId;
    private UUID foodId;
    private SimpleAppUserDTO commentator;
    private String comment;
    private Timestamp feedbackDate;
    private FeedbackDTO reply;
}
