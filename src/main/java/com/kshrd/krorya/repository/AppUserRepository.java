package com.kshrd.krorya.repository;
import com.kshrd.krorya.configuration.UUIDTypeHandler;
import com.kshrd.krorya.model.dto.SimpleAppUserDTO;
import com.kshrd.krorya.model.entity.AppUser;
import com.kshrd.krorya.model.entity.Follow;
import com.kshrd.krorya.model.request.AppUserRequest;
import com.kshrd.krorya.model.request.GoogleUserRequest;
import com.kshrd.krorya.model.request.UserRequest;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import java.util.List;
import java.util.UUID;

@Mapper
public interface AppUserRepository {
    @Results(id = "userMap", value = {
            @Result(property = "roles", column = "id",
                    many = @Many(select = "getRoleByUserId")
            ),
            @Result(property = "isDeactivated", column = "is_deactivated"),
            @Result(property = "userId", column = "user_id", jdbcType = JdbcType.OTHER, javaType = UUID.class, typeHandler = UUIDTypeHandler.class),
            @Result(property = "fullName", column = "full_name"),
            @Result(property = "profileImage", column = "profile_image"),
            @Result(property = "bio", column = "bio"),
            @Result(property = "followingsCount", column = "followings_count"),
            @Result(property = "followersCount", column = "follower_count")
//            @Result(property = "isFollowingByCurrentUser", column = "follows")
    })
    @Select("""
            SELECT * FROM users WHERE email = #{email}
            """)
    AppUser findByEmail(String email);

    @Select("""
                INSERT INTO users (username, email, password, profile_image, role, is_deactivated, followings_count, follower_count)
                VALUES (#{user.username}, #{user.email}, #{user.password}, #{user.profileImage}, 'user', FALSE, DEFAULT, DEFAULT)
                RETURNING *
            """)
    @ResultMap("userMap")
    AppUser saveUser(@Param("user") AppUserRequest appUserRequest);


    @Select("""
                    SELECT user_id,email,username,profile_image ,bio , followings_count, follower_count FROM users WHERE user_id = #{id}
            """)
    @ResultMap("userMap")
    AppUser selectUserById(UUID id);

    @Select("""
                INSERT INTO users (username, email, password, profile_image, role, is_deactivated, followings_count, follower_count)
                VALUES (#{user.username}, #{user.email}, #{user.password}, #{user.profileImage}, 'user', FALSE, DEFAULT, DEFAULT)
                RETURNING *
            """)
    @ResultMap("userMap")
    AppUser registerWithGoogle(@Param("user") GoogleUserRequest googleUserRequest);

    @Select("""
              select * from users
            """)
    @ResultMap("userMap")
    List<AppUser> findAllUser();

    @Select("""
            SELECT user_id, username, profile_image
            FROM users
            WHERE user_id = #{followerId}
            """)
    @Results(value = {
            @Result(property = "userId", column = "user_id"),
            @Result(property = "username", column = "username"),
            @Result(property = "profileImage", column = "profile_image")
    })
    SimpleAppUserDTO findUserByFollowerIdOrFollowingId(UUID followerId);

    @Select("""
            SELECT *
            FROM users
            WHERE user_id = #{userId}
            """)
    AppUser findAll();

    @Select("""
            SELECT * FROM users
            """)
    @ResultMap("userMap")
    List<AppUser> findAllUsers();

    @Select("SELECT COUNT(*) > 0 FROM users WHERE user_id = #{userId}")
    boolean existsById(UUID userId);

    @Select("SELECT is_deactivated FROM users WHERE user_id = #{userId}")
    boolean isDeactivated(UUID userId);

    @Select("""
            UPDATE users SET username = #{user.username}, profile_image = #{user.profileImage}, bio = #{user.bio}
            WHERE user_id = #{userId}
            RETURNING *
            """)
    @ResultMap("userMap")
    AppUser updateUserById(@Param("userId") UUID userId, @Param("user") UserRequest userRequest);


    @Select("""
                    SELECT * FROM users WHERE user_id = #{id}
            """)
    @ResultMap("userMap")
    AppUser getUserById(UUID id);

    @Update("""
            UPDATE users SET password = #{newPassword}
            WHERE user_id = #{userId}
            """)
    void resetPassword(String newPassword, @Param("userId") UUID userId);

    @Select("""
            select username from users
            where user_id = #{userId}
            """)
    String findUsernameByUserid(UUID userId);

    @Select("""
            SELECT user_id, username, profile_image
            FROM users
            WHERE user_id = #{usernameOfCurrentUser}
            """)
    @Results(value = {
            @Result(property = "userId", column = "user_id"),
            @Result(property = "username", column = "username"),
            @Result(property = "profileImage", column = "profile_image")
    })
    SimpleAppUserDTO getSimpleAppUserById(UUID usernameOfCurrentUser);


//    @Select("""
//    SELECT * FROM follows WHERE follower_id = #{followerId}
//    AND following_id = #{followingId}
//    """)
//    @Result(property = "followId", column = "follow_id", jdbcType = JdbcType.OTHER, javaType = UUID.class, typeHandler = UUIDTypeHandler.class)
//    @Result(property = "follower", column = "follower_id", jdbcType = JdbcType.OTHER, javaType = UUID.class, typeHandler = UUIDTypeHandler.class)
//    @Result(property = "following", column = "following_id", jdbcType = JdbcType.OTHER, javaType = UUID.class, typeHandler = UUIDTypeHandler.class)
//    Follow getFollowingByFollower(UUID followerId, UUID followingId);

}



