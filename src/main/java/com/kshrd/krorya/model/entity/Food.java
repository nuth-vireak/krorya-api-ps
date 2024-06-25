package com.kshrd.krorya.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kshrd.krorya.model.dto.SimpleAppUserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Food {
    private UUID foodId;
    private SimpleAppUserDTO sellerInfo;
    private UUID categoryId;
    private String foodName;
    private String foodDescription;
    private String foodImage;
    private String foodPrice;
    private Double starAverage;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private Integer totalRater;
    private Boolean isBookmarked;
}
