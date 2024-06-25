package com.kshrd.krorya.repository;

import com.kshrd.krorya.model.entity.Feedback;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.UUID;

@Mapper
public interface FeedbackRepository {

    @Select("""
            INSERT INTO feedbacks (parent_id, food_id, commentator, comment, feedback_date)
            VALUES (#{feedback.parentId}, #{feedback.foodId}, #{feedback.commentator}, #{feedback.comment}, CURRENT_TIMESTAMP)
            RETURNING *;
            """)
    @Results(id = "feedbackMap", value = {
            @Result(property = "feedbackId", column = "feedback_id"),
            @Result(property = "parentId", column = "parent_id"),
            @Result(property = "foodId", column = "food_id"),
            @Result(property = "commentator", column = "commentator"),
            @Result(property = "comment", column = "comment"),
            @Result(property = "feedbackDate", column = "feedback_date"),
            @Result(property = "reply", column = "feedback_id", one = @One(select = "com.kshrd.krorya.repository.FeedbackRepository.findFeedbackByParentId")),
    })
    Feedback insertFeedback(@Param("feedback") Feedback feedback);

    @Select("""
            SELECT * FROM feedbacks
            WHERE parent_id = #{parentId}
            """)
    @ResultMap("feedbackMap")
    Feedback findFeedbackByParentId(UUID parentId);

    @Select("""
            SELECT seller_id FROM foods
            WHERE food_id = #{foodId} AND seller_id = #{commentator}
            """)
    UUID isFoodOwner(@Param("foodId") UUID foodId, @Param("commentator") UUID commentator);

    @Select("""
            SELECT COUNT(*) FROM feedbacks
            WHERE parent_id = #{parentId}
            """)
    int countReply(@Param("parentId") UUID parentId);

    @Select("""
            SELECT * FROM feedbacks
            WHERE food_id = #{foodId} AND parent_id IS NULL
            """)
    @ResultMap("feedbackMap")
    List<Feedback> getFeedbacksByFoodId(@Param("foodId") UUID foodId);

    @Select("""
            SELECT COUNT(*) FROM foods
            WHERE food_id = #{foodId}
            """)
    boolean isFoodExist(UUID foodId);

    @Select("""
            SELECT COUNT(*) FROM feedbacks
            WHERE feedback_id = #{parentId}
            """)
    boolean existsById(UUID parentId);

    @Select("""
            SELECT * FROM feedbacks
            WHERE feedback_id = #{parentId}
            """)
    Feedback findById(UUID parentId);

    @Select("""
            SELECT parent_id FROM feedbacks
            WHERE feedback_id = #{parentId}
            """)
    UUID findParentIdByFeedbackId(UUID parentId);

    @Select("""
        SELECT seller_id
        FROM foods
        WHERE food_id = #{foodId}
        """)
    UUID isFoodOwnerInsert(@Param("foodId") UUID foodId);
}
