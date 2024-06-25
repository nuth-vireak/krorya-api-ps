package com.kshrd.krorya.repository;

import com.kshrd.krorya.configuration.UUIDTypeHandler;
import com.kshrd.krorya.model.entity.Payment;
import com.kshrd.krorya.model.enumeration.PaymentMethodEnum;
import org.apache.ibatis.annotations.*;

import java.util.UUID;

@Mapper
public interface PaymentRepository {

    @Select("""
    INSERT INTO payments (buyer_id, mode)
    VALUES (#{buyerId}, #{paymentMethod})
    RETURNING *;
    """)
    @Results(id = "paymentMapping", value = {
            @Result(property = "transactionId", column = "transaction_id", javaType = UUID.class, typeHandler = UUIDTypeHandler.class),
            @Result(property = "buyerId", column = "buyer_id", javaType = UUID.class, typeHandler = UUIDTypeHandler.class
                    /*,one = @One(select = "com.kshrd.krorya.repository.UserRepository.getUserById")*/),
            @Result(property = "paymentStatus", column = "payment_status"),
            @Result(property = "paymentMethod", column = "mode"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "updatedAt", column = "updated_at")
    })

    Payment createPayment(@Param("buyerId") UUID buyerId, @Param("paymentMethod") PaymentMethodEnum paymentMethod);

    @Select("""
    SELECT * FROM payments WHERE transaction_id = #{transactionId};
    """)
    @ResultMap("paymentMapping")
    Payment getPaymentById(UUID transactionId);

    @Select("""
    SELECT COUNT(*) FROM payments WHERE payment_status = 'paid';
    """)
    Integer countSuccessOrder();


    @Select("""
         SELECT COUNT(*) FROM payments WHERE payment_status = 'paid';
    """)
    Integer countTotalPaidOrder();

    @Select("""
         SELECT COUNT(*) FROM payments WHERE payment_status = 'unpaid';
    """)
    Integer countTotalUnPaidOrder();


}
