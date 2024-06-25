package com.kshrd.krorya.repository;

import com.kshrd.krorya.configuration.UUIDTypeHandler;
import com.kshrd.krorya.model.dto.OtpDTO;
import com.kshrd.krorya.model.entity.otp;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.UUID;

@Mapper
public interface OtpRepository {
//    @Insert("""
//            INSERT INTO otps (otp_code, expired_date, is_verified, user_id) VALUES (#{otp.otpCode}, #{otp.expiresAt}, false, #{otp.appUserDTO.userId})
//            """)
//    void createNewOtp(@Param("otp") OtpDTO otpsDTO);

//    @Insert("""
//            INSERT INTO otps (otp_code, expired_date, is_verified, user_id, is_verified_forget) VALUES (#{otp.otpCode}, #{otp.expiresAt}, false, #{otp.userId}, false)
//            """)
//    void createNewOtp(@Param("otp") OtpDTO otpsDTO);

    @Insert("""
        INSERT INTO otps (otp_code, expired_date, is_verified, user_id, is_verified_forget)
        VALUES (#{otp.otpCode}, #{otp.expiresAt}, #{otp.verify}, #{otp.userId}, #{otp.isVerifiedForget})
    """)
    void createNewOtp(@Param("otp") OtpDTO otpDTO);

    @Results(id = "otp", value = {
            @Result(column = "user_id", property = "userId", javaType = UUID.class, jdbcType = JdbcType.OTHER, typeHandler = UUIDTypeHandler.class),
            @Result(property = "issuedDate", column = "issued_at"),
            @Result(property = "otpCode", column = "otp_code"),
            @Result(property = "verify", column = "is_verified"),
            @Result(property = "isVerifiedForget", column = "is_verified_forget"),
            @Result(property = "expiresAt", column = "expired_date"),    })
    @Select("""
            SELECT * FROM otps WHERE otp_code = #{code}
            """)
    OtpDTO findByCode(String code);

    @Update("""
            UPDATE otps SET is_verified = true WHERE otp_code = #{code}
            """)
    void verify(String code);

    @Update("""
            UPDATE otps SET is_verified_forget = true WHERE otp_code = #{code}
            """)
    void verifyForgetPassword(String code);

    default otp findOtpByUserId(UUID userId) {
        System.out.println("in findOtpByUserId : " + userId) ;
        System.out.println(selectOtpByUserId(userId));
        return selectOtpByUserId(userId);
    }

    @Select("""
            SELECT * FROM otps WHERE user_id = #{userId}
            """)
    @Results(id = "otpMap", value = {
            @Result(column = "otp_id", property = "otpId", javaType = UUID.class, jdbcType = JdbcType.OTHER, typeHandler = UUIDTypeHandler.class),
            @Result(column = "otp_code", property = "otpCode"),
            @Result(column = "issued_at", property = "issuedAt"),
            @Result(column = "expired_date", property = "expiresAt"),
            @Result(column = "is_verified", property = "verify"),
            @Result(column = "user_id", property = "userId", javaType = UUID.class, jdbcType = JdbcType.OTHER, typeHandler = UUIDTypeHandler.class),
            @Result(column = "user_id", property = "appUser", one = @One(select = "com.kshrd.krorya.repository.AppUserRepository.selectUserById")),
            @Result(column = "is_verified_forget", property = "isVerifiedForget")
    })
    otp selectOtpByUserId(UUID userId);

    @Select("""
            SELECT * FROM otps WHERE user_id = (SELECT users.user_id FROM users WHERE email = #{email})
            """)
    @ResultMap("otpMap")
    otp findByEmail(String email);

    @Update("""
            UPDATE otps SET otp_code = #{otp.otpCode}, expired_date = #{otp.expiresAt}, is_verified = #{otp.verify} WHERE user_id = #{otp.userId}
            """)
    void update(@Param("otp") OtpDTO existingOtp);

    @Update("""
            UPDATE users SET password = #{newPassword}
            WHERE email = #{email}
            """)
    void changePassword(String newPassword, String email);
    @Update("""
            UPDATE otps SET is_verified_forget = false WHERE otp_code = #{otpCode}
            """)
    void verifyForgetPasswordToFalse(String otpCode);
}
