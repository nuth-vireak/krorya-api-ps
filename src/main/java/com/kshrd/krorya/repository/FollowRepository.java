package com.kshrd.krorya.repository;

import com.kshrd.krorya.configuration.UUIDTypeHandler;
import com.kshrd.krorya.model.dto.FollowDTO;
import com.kshrd.krorya.model.entity.Follow;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
import java.util.UUID;

@Mapper
public interface FollowRepository {

    @Select("""
            INSERT INTO follows (following_id, follower_id)
            VALUES (#{followingId}, #{followerId})
            RETURNING *
            """)
    @Results(id = "followMap", value = {
            @Result(property = "followId", column = "follow_id"),
            @Result(property = "follower", column = "follower_id", one = @One(select = "com.kshrd.krorya.repository.AppUserRepository.findUserByFollowerIdOrFollowingId")),
            @Result(property = "following", column = "following_id", one = @One(select = "com.kshrd.krorya.repository.AppUserRepository.findUserByFollowerIdOrFollowingId")),
    })
    Follow insertFollow(@Param("followerId") UUID followerId, @Param("followingId") UUID followingId);

    @Select("""
            SELECT COUNT(*) FROM follows
            WHERE follower_id = #{followerId} AND following_id = #{followingId}
            """)
    int countFollowByUserIds(@Param("followerId") UUID followerId, @Param("followingId") UUID followingId);

    @Select("""
            DELETE FROM follows
            WHERE follower_id = #{followerId} AND following_id = #{followingId}
            RETURNING *
            """)
    @ResultMap("followMap")
    Follow deleteFollow(@Param("followerId") UUID followerId, @Param("followingId") UUID followingId);

    @Select("""
            SELECT f.follow_id,
                   follower.user_id AS follower_id, follower.username AS follower_username, follower.profile_image AS follower_profile_image
            FROM follows f
            JOIN users follower ON f.follower_id = follower.user_id
            WHERE f.following_id = #{userId}
            """)
    @Results(id = "allFollowerMap", value = {
            @Result(property = "followId", column = "follow_id"),
            @Result(property = "follower.userId", column = "follower_id"),
            @Result(property = "follower.username", column = "follower_username"),
            @Result(property = "follower.profileImage", column = "follower_profile_image"),
    })
    List<Follow> findAllFollowersByUserId(UUID userId);

    @Select("""
            SELECT f.follow_id,
                   following.user_id AS following_id, following.username AS following_username, following.profile_image AS following_profile_image
            FROM follows f
            JOIN users following ON f.following_id = following.user_id
            WHERE f.follower_id = #{userId}
            """)
    @Results(id = "allFollowingMap", value = {
            @Result(property = "followId", column = "follow_id"),
            @Result(property = "following.userId", column = "following_id"),
            @Result(property = "following.username", column = "following_username"),
            @Result(property = "following.profileImage", column = "following_profile_image"),
    })
    List<Follow> findAllFollowingsByUserId(UUID userId);

    @Select("""
    SELECT * FROM follows WHERE follower_id = #{followerId}
    AND following_id = #{followingId}
    """)
//    @Result(property = "followId", column = "follow_id", jdbcType = JdbcType.OTHER, javaType = UUID.class, typeHandler = UUIDTypeHandler.class)
//    @Result(property = "follower", column = "follower_id", jdbcType = JdbcType.OTHER, javaType = UUID.class, typeHandler = UUIDTypeHandler.class)
//    @Result(property = "following", column = "following_id", jdbcType = JdbcType.OTHER, javaType = UUID.class, typeHandler = UUIDTypeHandler.class)
    @ResultMap("followMap")
    Follow getFollowingByFollower(UUID followerId, UUID followingId);
}
