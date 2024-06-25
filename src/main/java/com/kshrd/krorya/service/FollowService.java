package com.kshrd.krorya.service;

import com.kshrd.krorya.model.dto.FollowDTO;

import java.util.List;
import java.util.UUID;

public interface FollowService {
    FollowDTO followerUserByUserId(UUID followingId);
    FollowDTO unfollowUserById(UUID followingId);
    List<FollowDTO> getFollowersByUserId(UUID userId, UUID currentUserId);
    List<FollowDTO> getFollowingsByUserId(UUID userId, UUID currentUserId);
}
