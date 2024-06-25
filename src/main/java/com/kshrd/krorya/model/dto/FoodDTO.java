package com.kshrd.krorya.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FoodDTO {
    private UUID foodId;
    private String foodName;
    private SimpleAppUserDTO sellerInfo;
    private String foodDescription;
    private String foodImage;
    private String foodPrice;
    private Float starAverage;
    private Integer totalRater;
    private Boolean isBookmarked;
    private LocalDateTime createAt;
}
