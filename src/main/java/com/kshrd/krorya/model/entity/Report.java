package com.kshrd.krorya.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kshrd.krorya.model.dto.SimpleAppUserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Report {
    private UUID reportId;
    private Recipe recipe;
    private SimpleAppUserDTO reporterInfo;
    private SimpleAppUserDTO reporteeInfo;
    private String description;
    private Date reportDate;
    private String reportType;
}
