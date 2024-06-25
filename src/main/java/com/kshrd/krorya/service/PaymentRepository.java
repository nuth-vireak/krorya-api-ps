package com.kshrd.krorya.service;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PaymentRepository {


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
