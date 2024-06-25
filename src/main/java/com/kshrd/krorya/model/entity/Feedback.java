package com.kshrd.krorya.model.entity;

import com.kshrd.krorya.model.dto.SimpleAppUserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Feedback {
    private UUID feedbackId;
    private UUID parentId;
    private UUID foodId;
    private UUID commentator;
    private String comment;
    private Timestamp feedbackDate;
    private Feedback reply;
}
