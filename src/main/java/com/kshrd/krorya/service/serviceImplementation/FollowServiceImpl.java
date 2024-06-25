package com.kshrd.krorya.service.serviceImplementation;

import com.kshrd.krorya.convert.AppUserConvertor;
import com.kshrd.krorya.convert.FollowDTOConvertor;
import com.kshrd.krorya.exception.CustomNotFoundException;
import com.kshrd.krorya.model.dto.AppUserDTO;
import com.kshrd.krorya.model.dto.FollowDTO;
import com.kshrd.krorya.model.entity.AppUser;
import com.kshrd.krorya.model.entity.CustomUserDetail;
import com.kshrd.krorya.model.entity.Follow;
import com.kshrd.krorya.repository.AppUserRepository;
import com.kshrd.krorya.repository.FollowRepository;
import com.kshrd.krorya.service.FollowService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FollowServiceImpl implements FollowService {

    private static final Logger log = LoggerFactory.getLogger(FollowServiceImpl.class);
    private final FollowRepository followRepository;
    private final AppUserRepository appUserRepository;
    private final FollowDTOConvertor followDTOConvertor;
    private final AppUserConvertor appUserConvertor;

    @Transactional
    @Override
    public FollowDTO followerUserByUserId(UUID followingId) {

        UUID followerId = getCurrentUserId();

        if (followerId.equals(followingId)) {
            throw new IllegalStateException("You cannot follow yourself.");
        }
        if (!appUserRepository.existsById(followingId)) {
            throw new IllegalStateException("The user you are trying to follow does not exist.");
        }
        if (followRepository.countFollowByUserIds(followerId, followingId) > 0) {
            throw new IllegalStateException("You are already following this user.");
        }
        if (appUserRepository.isDeactivated(followingId)) {
            throw new IllegalStateException("The user you are trying to follow is deactivated.");
        }

        Follow insertedFollow = followRepository.insertFollow(followerId, followingId);
        log.info("Inserted follow: {}", insertedFollow);
        return followDTOConvertor.convertFollowToFollowerDTO(insertedFollow);
    }

    @Override
    public FollowDTO unfollowUserById(UUID followingId) {

        UUID currentUserId = getCurrentUserId();

        if (currentUserId.equals(followingId)) {
            throw new IllegalStateException("You cannot unfollow yourself.");
        }
        if (!appUserRepository.existsById(followingId)) {
            throw new IllegalStateException("The user you are trying to unfollow does not exist.");
        }
        if (followRepository.countFollowByUserIds(currentUserId, followingId) == 0) {
            throw new IllegalStateException("You are not following this user.");
        }

        Follow deletedFollow = followRepository.deleteFollow(currentUserId, followingId);
        log.info("Deleted follow: {}", deletedFollow);
        return followDTOConvertor.convertFollowToFollowerDTO(deletedFollow);
    }

//    @Override
//    public List<FollowDTO> getFollowersByUserId(UUID userId) {
//        List<Follow> followers = followRepository.findAllFollowersByUserId(userId);
//        if (followers == null) {
//            return Collections.emptyList();
//        }
//        return followers.stream()
//                .map(followDTOConvertor::convertFollowToFollowerDTO)
//                .collect(Collectors.toList());
//    }

    @Override
    public List<FollowDTO> getFollowersByUserId(UUID userId, UUID currentUserId) {
        List<Follow> followers = followRepository.findAllFollowersByUserId(userId);
        if (followers == null) {
            return Collections.emptyList();
        }

        List<Follow> currentUserFollowings = followRepository.findAllFollowingsByUserId(currentUserId);
        Set<UUID> currentUserFollowingIds = currentUserFollowings.stream()
                .map(follow -> follow.getFollowing().getUserId())
                .collect(Collectors.toSet());

        return followers.stream()
                .map(follow -> {
                    FollowDTO followDTO = followDTOConvertor.convertFollowToFollowerDTO(follow);
                    followDTO.setFollowingByCurrentUser(currentUserFollowingIds.contains(follow.getFollower().getUserId()));
                    return followDTO;
                })
                .collect(Collectors.toList());
    }

//    @Override
//    public List<FollowDTO> getFollowingsByUserId(UUID userId, UUID currentUserId) {
//        List<Follow> followings = followRepository.findAllFollowingsByUserId(userId);
//        if (followings == null) {
//            return Collections.emptyList();
//        };
//        return followings.stream()
//                .map(followDTOConvertor::convertFollowToFollowerDTO)
//                .collect(Collectors.toList());
//    }

    @Override
    public List<FollowDTO> getFollowingsByUserId(UUID userId, UUID currentUserId) {
        List<Follow> followings = followRepository.findAllFollowingsByUserId(userId);
        if (followings == null) {
            return Collections.emptyList();
        }

        List<Follow> currentUserFollowings = followRepository.findAllFollowingsByUserId(currentUserId);
        Set<UUID> currentUserFollowingIds = currentUserFollowings.stream()
                .map(follow -> follow.getFollowing().getUserId())
                .collect(Collectors.toSet());

        return followings.stream()
                .map(follow -> {
                    FollowDTO followDTO = followDTOConvertor.convertFollowToFollowerDTO(follow);
                    followDTO.setFollowingByCurrentUser(currentUserFollowingIds.contains(follow.getFollowing().getUserId()));
                    return followDTO;
                })
                .collect(Collectors.toList());
    }

    private UUID getCurrentUserId() {
        CustomUserDetail userDetails = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getAppUser().getUserId();
    }
}
