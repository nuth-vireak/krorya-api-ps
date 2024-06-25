package com.kshrd.krorya.repository;

import com.kshrd.krorya.configuration.UUIDTypeHandler;
import com.kshrd.krorya.model.entity.Address;
import com.kshrd.krorya.model.entity.Cart;
import com.kshrd.krorya.model.entity.Order;
import com.kshrd.krorya.model.entity.Payment;
import com.kshrd.krorya.model.enumeration.IsOrderRequestEnum;
import com.kshrd.krorya.model.enumeration.OrderStatusEnum;
import com.kshrd.krorya.model.enumeration.PaymentMethodEnum;
import com.kshrd.krorya.model.request.OrderRequest;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.UUID;

@Mapper
public interface OrderRepository {


    @Select("""
            INSERT INTO orders (cart_id, payment_id, address_id, description, total_amount, phone_number)
            VALUES (#{order.cartId}, #{paymentId}, #{order.addressId}, #{order.description}, #{order.totalAmount}, #{order.phoneNumber})
            RETURNING *;
            """)
    @Results(id = "orderMapping", value = {
            @Result(property = "orderId", column = "order_id"),
            @Result(property = "cartId", column = "cart_id"
                    , javaType = UUID.class, typeHandler = UUIDTypeHandler.class
                    ,one = @One(select = "com.kshrd.krorya.repository.CartRepository.getCartByCartIdNoCurrentUser")
            ),
            @Result(property = "paymentId", column = "payment_id"),
            @Result(property = "addressId", column = "address_id"),
            @Result(property = "totalAmount", column = "total_amount"),
            @Result(property = "phoneNumber", column = "phone_number"),
            @Result(property = "status", column = "status"),
            @Result(property = "isOrderRequest", column = "is_order_request"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "updatedAt", column = "updated_at")
    })
    Order createOrder(@Param("order") OrderRequest orderRequest, @Param("paymentId") UUID paymentId);

    @Select("""
            SELECT * FROM orders;
            """)
    @ResultMap("orderMapping")
    List<Order> getAllOrders();
    Order insertOrder();

    @Select("""
      SELECT COUNT(*) FROM orders;
""")
    Integer countAllFoods();


    @Select("""
    SELECT COUNT(*) FROM orders WHERE is_order_request = 'pending';
    """)
    Integer countNewOrder();


    @Select("""
     SELECT COUNT(*) FROM orders WHERE is_order_request = 'rejected';
     """)
    Integer countCancelOrder();

    @Select("""
    SELECT SUM(total_amount) FROM orders;
    """)
    Integer countTotalPayment();


    @Select("""
            SELECT COUNT(*) FROM orders where status = 'មិនទាន់ចម្អិន';
            """)
    Integer countTotalPrepareOrder();


    @Select("""
            SELECT COUNT(*) FROM orders where status = 'កំពុងចម្អិន';
    """)
    Integer countTotalCookingOrder();


    @Select("""
            SELECT COUNT(*) FROM orders where status = 'ចម្អិនរួចរាល់';
    """)
    Integer countTotalCookedOrder();
}
