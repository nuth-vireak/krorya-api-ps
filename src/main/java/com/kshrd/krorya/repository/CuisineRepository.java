package com.kshrd.krorya.repository;

import com.kshrd.krorya.configuration.UUIDTypeHandler;
import com.kshrd.krorya.model.entity.Cuisines;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
import java.util.UUID;

@Mapper
public interface CuisineRepository {
    @Results(id = "cuisinesMapping", value = {
            @Result(property = "cuisineId", column = "cuisine_id", jdbcType = JdbcType.OTHER, javaType = UUID.class, typeHandler = UUIDTypeHandler.class),
            @Result(property = "cuisineName", column = "name"),
    })
    @Select("""
            SELECT * FROM cuisines
            """)
    List<Cuisines> getCuisines();

    @Select("SELECT * FROM cuisines WHERE cuisine_id = #{id}")
    @ResultMap("cuisinesMapping")
    Cuisines selectCuisineById(UUID id);

    @Select("SELECT  name FROM cuisines WHERE cuisine_id = #{id}")
    String selectCuisineNameById(UUID id);


    @Select("""
              SELECT * FROM cuisines WHERE cuisine_id = #{cuisineId}
              """)
    @ResultMap("cuisinesMapping")
    Cuisines getCuisineById(UUID cuisineId);
}
