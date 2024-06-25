package com.kshrd.krorya.model.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
    private UUID tagId;
    @NotBlank(message = "Tag name is mandatory")
    private String tagName;
    private String icons;
}
