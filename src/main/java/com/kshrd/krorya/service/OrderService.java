package com.kshrd.krorya.service;

import com.kshrd.krorya.model.entity.Order;
import com.kshrd.krorya.model.enumeration.IsOrderRequestEnum;
import com.kshrd.krorya.model.enumeration.OrderStatusEnum;
import com.kshrd.krorya.model.enumeration.PaymentMethodEnum;
import com.kshrd.krorya.model.request.OrderRequest;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    Order createOrder(OrderRequest orderRequest, PaymentMethodEnum paymentMethod);

    List<Order> getAllOrders();
}
