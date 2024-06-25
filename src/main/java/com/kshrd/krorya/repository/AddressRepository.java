package com.kshrd.krorya.repository;

import com.kshrd.krorya.model.entity.Address;
import com.kshrd.krorya.model.request.AddressRequest;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.UUID;

@Mapper
public interface AddressRepository {

    @Select("""
    INSERT INTO addresses (user_id, location, latitude, longitude) 
    VALUES (#{userId}, #{address.location}, #{address.latitude}, #{address.longitude})
    RETURNING *;
    """)
    @Results(id = "addressMapping", value = {
            @Result(property = "addressId", column = "address_id"),
            @Result(property = "location", column = "location"),
            @Result(property = "buyerLatitude", column = "latitude"),
            @Result(property = "buyerLongitude", column = "longitude"),
    })
    Address insertAddress(@Param("address") AddressRequest addressRequest, @Param("userId") UUID userId);

    @Select("""
    SELECT username FROM users WHERE user_id = #{userId}
    """)
    String getUserNameByUserId(UUID userId);

    @Select("""
    SELECT * FROM addresses WHERE user_id = #{userId}
    """)
       @Results(id= "addressUserMapping", value ={
               @Result(property = "addressId", column = "address_id"),
               @Result(property = "location", column = "location"),
               @Result(property = "buyerLatitude", column = "latitude"),
               @Result(property = "buyerLongitude", column = "longitude"),
       })
    List<Address> getAllAddressesByUserId(@Param("userId") UUID userId);

    @Select("""
    SELECT * FROM addresses WHERE user_id = #{userId} AND address_id = #{addressId}
    """)
    @ResultMap("addressMapping")
    Address getAddressByAddressId(UUID addressId, @Param("userId") UUID userId);


    @Select("""
    UPDATE addresses
    SET latitude = #{address.latitude},
        longitude = #{address.longitude}
    WHERE user_id = #{userId} AND address_id = #{addressId}
    RETURNING *
    """)
    @ResultMap("addressMapping")
    Address updateAddressByAddressId(@Param("address") AddressRequest addressRequest, UUID addressId, @Param("userId") UUID userId);

    @Select("""
    DELETE FROM addresses WHERE user_id = #{userId} AND address_id = #{addressId}
    """)
    @ResultMap("addressMapping")
    Address deleteAddressByAddressId(UUID addressId, UUID userId);
}

