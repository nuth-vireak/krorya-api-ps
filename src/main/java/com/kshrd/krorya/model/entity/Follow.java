package com.kshrd.krorya.model.entity;

import com.kshrd.krorya.model.dto.AppUserDTO;
import com.kshrd.krorya.model.dto.SimpleAppUserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Follow {
    private UUID followId;
    private SimpleAppUserDTO follower;
    private SimpleAppUserDTO following;
}
