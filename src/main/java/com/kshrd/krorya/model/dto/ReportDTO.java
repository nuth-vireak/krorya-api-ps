package com.kshrd.krorya.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kshrd.krorya.model.entity.AppUser;
import com.kshrd.krorya.model.entity.Recipe;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportDTO {
    private UUID reportId;
    private Recipe recipe;
    private SimpleAppUserDTO reporterInfo;
    private SimpleAppUserDTO reporteeInfo;
    private String description;
    private Date reportDate;
    //private String reportType;
}
